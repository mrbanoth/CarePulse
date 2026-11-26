package com.example.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.mock.AppNotification
import com.example.data.mock.Appointment
import com.example.data.mock.CheckupSchedule
import com.example.data.mock.ConsultationType
import com.example.data.mock.Doctor
import com.example.data.mock.PartnerClinic
import com.example.data.mock.PatientProfile
import com.example.data.mock.Prescription
import com.example.data.mock.SpecialtyCategory
import com.example.data.repository.DoctorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VideoCallUiState(
  val doctor: Doctor? = null,
  val elapsedSeconds: Long = 850L,
  val isMuted: Boolean = false,
  val isSpeakerOn: Boolean = true,
  val isCameraOn: Boolean = true,
  val isScreenShared: Boolean = false,
)

data class DoctorAppUiState(
  val homeProfile: PatientProfile? = null,
  val treatmentPatient: PatientProfile? = null,
  val specialties: List<SpecialtyCategory> = emptyList(),
  val allDoctors: List<Doctor> = emptyList(),
  val filteredDoctors: List<Doctor> = emptyList(),
  val recentVisitDoctors: List<Doctor> = emptyList(),
  val recommendedDoctors: List<Doctor> = emptyList(),
  val appointments: List<Appointment> = emptyList(),
  val checkupSchedules: List<CheckupSchedule> = emptyList(),
  val prescriptions: List<Prescription> = emptyList(),
  val clinics: List<PartnerClinic> = emptyList(),
  val notifications: List<AppNotification> = emptyList(),
  val unreadNotificationCount: Int = 0,
  val searchQuery: String = "",
  val selectedSpecialtyId: String? = null,
  val liveSyncTick: Long = 0L,
  val bookingTargetDoctor: Doctor? = null,
  val showNotificationsSheet: Boolean = false,
  val showTreatmentPlansSheet: Boolean = false,
  val selectedCheckup: CheckupSchedule? = null,
  val selectedAppointment: Appointment? = null,
  val statusBannerMessage: String? = null,
)

private data class FilterAndModalState(
  val searchQuery: String = "",
  val selectedSpecialtyId: String? = null,
  val bookingTargetDoctor: Doctor? = null,
  val showNotificationsSheet: Boolean = false,
  val showTreatmentPlansSheet: Boolean = false,
  val selectedCheckup: CheckupSchedule? = null,
  val selectedAppointment: Appointment? = null,
  val statusBannerMessage: String? = null,
)

class DoctorAppViewModel(
  private val repository: DoctorRepository = DoctorRepository()
) : ViewModel() {

  private val localState = MutableStateFlow(FilterAndModalState())

  private val _videoCallState = MutableStateFlow(VideoCallUiState())
  val videoCallState: StateFlow<VideoCallUiState> = _videoCallState

  private var callTimerJob: Job? = null

  private val coreDataFlow =
    combine(
      repository.homeProfile,
      repository.treatmentPatient,
      repository.doctors,
      repository.appointments,
      repository.checkupSchedules,
    ) { homeProfile, treatmentPatient, doctors, appointments, checkups ->
      CoreSnapshot(homeProfile, treatmentPatient, doctors, appointments, checkups)
    }

  private val auxiliaryDataFlow =
    combine(
      repository.prescriptions,
      repository.clinics,
      repository.notifications,
      repository.liveSyncTick,
    ) { prescriptions, clinics, notifications, tick ->
      AuxSnapshot(prescriptions, clinics, notifications, tick)
    }

  val uiState: StateFlow<DoctorAppUiState> =
    combine(coreDataFlow, auxiliaryDataFlow, localState) { core, aux, local ->
      val query = local.searchQuery.trim().lowercase()
      val selectedSpec = local.selectedSpecialtyId
      val filtered =
        core.doctors.filter { doc ->
          val matchesSpec = selectedSpec == null || doc.specialtyId == selectedSpec
          val matchesQuery =
            query.isEmpty() ||
              doc.name.lowercase().contains(query) ||
              doc.specialtyName.lowercase().contains(query) ||
              doc.hospitalName.lowercase().contains(query)
          matchesSpec && matchesQuery
        }

      DoctorAppUiState(
        homeProfile = core.homeProfile,
        treatmentPatient = core.treatmentPatient,
        specialties = repository.specialties,
        allDoctors = core.doctors,
        filteredDoctors = filtered,
        recentVisitDoctors = core.doctors.filter { it.isRecentVisit },
        recommendedDoctors = core.doctors.filter { it.isRecommended },
        appointments = core.appointments,
        checkupSchedules = core.checkups,
        prescriptions = aux.prescriptions,
        clinics = aux.clinics,
        notifications = aux.notifications,
        unreadNotificationCount = aux.notifications.count { it.isUnread },
        searchQuery = local.searchQuery,
        selectedSpecialtyId = local.selectedSpecialtyId,
        liveSyncTick = aux.tick,
        bookingTargetDoctor = local.bookingTargetDoctor,
        showNotificationsSheet = local.showNotificationsSheet,
        showTreatmentPlansSheet = local.showTreatmentPlansSheet,
        selectedCheckup = local.selectedCheckup,
        selectedAppointment = local.selectedAppointment,
        statusBannerMessage = local.statusBannerMessage,
      )
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      initialValue = DoctorAppUiState(),
    )

  fun onSearchQueryChanged(query: String) {
    localState.update { it.copy(searchQuery = query) }
  }

  fun onSpecialtyToggled(specialtyId: String) {
    localState.update { state ->
      val next = if (state.selectedSpecialtyId == specialtyId) null else specialtyId
      state.copy(selectedSpecialtyId = next)
    }
  }

  fun clearFilters() {
    localState.update { it.copy(searchQuery = "", selectedSpecialtyId = null) }
  }

  fun openBookingSheet(doctor: Doctor) {
    localState.update { it.copy(bookingTargetDoctor = doctor) }
  }

  fun dismissBookingSheet() {
    localState.update { it.copy(bookingTargetDoctor = null) }
  }

  fun confirmBooking(
    doctor: Doctor,
    dateLabel: String,
    dayNumber: String,
    monthShort: String,
    timeSlot: String,
    type: ConsultationType,
  ) {
    repository.bookAppointment(doctor, dateLabel, dayNumber, monthShort, timeSlot, type)
    localState.update {
      it.copy(
        bookingTargetDoctor = null,
        statusBannerMessage = "Booked ${doctor.name} • $dateLabel ($timeSlot)",
      )
    }
  }

  fun openNotificationsSheet() {
    localState.update { it.copy(showNotificationsSheet = true) }
  }

  fun dismissNotificationsSheet() {
    localState.update { it.copy(showNotificationsSheet = false) }
  }

  fun markAllNotificationsRead() {
    repository.markAllNotificationsRead()
  }

  fun openTreatmentPlansSheet() {
    localState.update { it.copy(showTreatmentPlansSheet = true) }
  }

  fun dismissTreatmentPlansSheet() {
    localState.update { it.copy(showTreatmentPlansSheet = false) }
  }

  fun openCheckupDetail(checkup: CheckupSchedule) {
    localState.update { it.copy(selectedCheckup = checkup) }
  }

  fun dismissCheckupDetail() {
    localState.update { it.copy(selectedCheckup = null) }
  }

  fun toggleCheckupCompleted(checkupId: String) {
    repository.toggleCheckupCompleted(checkupId)
    localState.update { it.copy(selectedCheckup = null, statusBannerMessage = "Checkup status updated") }
  }

  fun openAppointmentDetail(appointment: Appointment) {
    localState.update { it.copy(selectedAppointment = appointment) }
  }

  fun dismissAppointmentDetail() {
    localState.update { it.copy(selectedAppointment = null) }
  }

  fun cancelAppointment(appointmentId: String) {
    repository.cancelAppointment(appointmentId)
    localState.update {
      it.copy(selectedAppointment = null, statusBannerMessage = "Appointment cancelled")
    }
  }

  fun togglePrescriptionTaken(id: String) {
    repository.togglePrescriptionTaken(id)
  }

  fun requestPrescriptionRefill(id: String) {
    repository.requestPrescriptionRefill(id)
    localState.update { it.copy(statusBannerMessage = "Pharmacy refill request sent") }
  }

  fun switchTreatmentPatient() {
    repository.switchTreatmentPatient()
    localState.update { it.copy(statusBannerMessage = "Switched active treatment patient") }
  }

  fun dismissStatusBanner() {
    localState.update { it.copy(statusBannerMessage = null) }
  }

  fun findDoctorById(doctorId: String): Doctor? {
    return repository.doctors.value.firstOrNull { it.id == doctorId }
  }

  fun startVideoCall(doctorId: String?) {
    val doctors = repository.doctors.value
    val target =
      doctors.firstOrNull { it.id == doctorId }
        ?: doctors.firstOrNull { it.id == "doc_nick" }
        ?: doctors.firstOrNull()
    val startSeconds = if (target?.id == "doc_nick") 850L else 1L
    _videoCallState.value =
      VideoCallUiState(
        doctor = target,
        elapsedSeconds = startSeconds,
        isMuted = false,
        isSpeakerOn = true,
        isCameraOn = true,
        isScreenShared = false,
      )
    callTimerJob?.cancel()
    callTimerJob =
      viewModelScope.launch {
        while (true) {
          delay(1000L)
          _videoCallState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1L) }
        }
      }
  }

  fun toggleMute() {
    _videoCallState.update { it.copy(isMuted = !it.isMuted) }
  }

  fun toggleSpeaker() {
    _videoCallState.update { it.copy(isSpeakerOn = !it.isSpeakerOn) }
  }

  fun toggleCamera() {
    _videoCallState.update { it.copy(isCameraOn = !it.isCameraOn) }
  }

  fun toggleScreenShare() {
    _videoCallState.update { it.copy(isScreenShared = !it.isScreenShared) }
  }

  fun endVideoCall() {
    callTimerJob?.cancel()
    callTimerJob = null
    localState.update { it.copy(statusBannerMessage = "Consultation call ended") }
  }

  override fun onCleared() {
    super.onCleared()
    callTimerJob?.cancel()
  }

  private data class CoreSnapshot(
    val homeProfile: PatientProfile,
    val treatmentPatient: PatientProfile,
    val doctors: List<Doctor>,
    val appointments: List<Appointment>,
    val checkups: List<CheckupSchedule>,
  )

  private data class AuxSnapshot(
    val prescriptions: List<Prescription>,
    val clinics: List<PartnerClinic>,
    val notifications: List<AppNotification>,
    val tick: Long,
  )
}
