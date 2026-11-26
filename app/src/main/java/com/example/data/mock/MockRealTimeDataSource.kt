package com.example.data.mock

import com.example.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Local real-time mock data engine stored in data/mock/ (no backend required).
 * Provides real-time image URLs (with bundled drawable fallbacks), live doctor status updates,
 * clinic wait-time ticks, treatment progress updates, and instant reactive mutations.
 */
object MockRealTimeDataSource {
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

  val specialties: List<SpecialtyCategory> =
    listOf(
      SpecialtyCategory(
        id = "neuro",
        name = "Neurologist",
        iconType = SpecialtyIconType.NEUROLOGY,
        accentColorHex = 0xFFEF6C6C,
      ),
      SpecialtyCategory(
        id = "cardio",
        name = "Cardiologist",
        iconType = SpecialtyIconType.CARDIOLOGY,
        accentColorHex = 0xFF56B7EE,
      ),
      SpecialtyCategory(
        id = "ortho",
        name = "Orthopedist",
        iconType = SpecialtyIconType.ORTHOPEDICS,
        accentColorHex = 0xFFF3A157,
      ),
      SpecialtyCategory(
        id = "pulmo",
        name = "Pulmonologist",
        iconType = SpecialtyIconType.PULMONOLOGY,
        accentColorHex = 0xFF7371D4,
      ),
      SpecialtyCategory(
        id = "podiatry",
        name = "Podiatrist",
        iconType = SpecialtyIconType.PODIATRY,
        accentColorHex = 0xFF39B2A8,
      ),
    )

  private val initialPatients =
    listOf(
      PatientProfile(
        id = "pat_siyam",
        displayName = "Siyam Ahamed",
        firstName = "Siyam Ahamed!",
        genderAgeLabel = "Male, 27y.o",
        heightLabel = "Height: 5.9\"",
        bloodType = "O+",
        isPro = true,
        avatarUrl = "https://randomuser.me/api/portraits/men/32.jpg",
        avatarRes = R.drawable.img_patient_avatar,
        treatmentPlans =
          listOf(
            TreatmentPlan(
              id = "tp_1",
              title = "Foot & Ankle Mobility Recovery",
              doctorName = "Dr. Jennifer Smith",
              progressPercent = 0.72f,
              nextMilestone = "Ankle stability checkup • Sep 07",
              durationText = "6 Weeks Plan",
            ),
            TreatmentPlan(
              id = "tp_2",
              title = "Migraine Prevention Protocol",
              doctorName = "Dr. Warner",
              progressPercent = 0.45f,
              nextMilestone = "Sleep & dosage review • Sep 14",
              durationText = "8 Weeks Plan",
            ),
            TreatmentPlan(
              id = "tp_3",
              title = "Respiratory Endurance Care",
              doctorName = "Dr. Roman Novara",
              progressPercent = 0.85f,
              nextMilestone = "Final spirometry test • Sep 21",
              durationText = "4 Weeks Plan",
            ),
          ),
      ),
      PatientProfile(
        id = "pat_esther",
        displayName = "Esther Howard",
        firstName = "Esther Howard!",
        genderAgeLabel = "Female, 23y.o",
        heightLabel = "Height: 5.4\"",
        bloodType = "A+",
        isPro = true,
        avatarUrl = "https://randomuser.me/api/portraits/women/44.jpg",
        avatarRes = R.drawable.img_patient_avatar,
        treatmentPlans =
          listOf(
            TreatmentPlan(
              id = "tp_e1",
              title = "Post-Sprain Rehabilitation",
              doctorName = "Dr. Jennifer Smith",
              progressPercent = 0.65f,
              nextMilestone = "Clinic physical assessment • Sep 07",
              durationText = "6 Weeks Plan",
            ),
            TreatmentPlan(
              id = "tp_e2",
              title = "Seasonal Asthma Control",
              doctorName = "Dr. Roman Novara",
              progressPercent = 0.80f,
              nextMilestone = "Inhaler technique check • Sep 12",
              durationText = "12 Weeks Plan",
            ),
            TreatmentPlan(
              id = "tp_e3",
              title = "Podiatry Arch Support Therapy",
              doctorName = "Dr. Nick Tylor",
              progressPercent = 0.50f,
              nextMilestone = "Video gait evaluation • Sep 18",
              durationText = "4 Weeks Plan",
            ),
          ),
      ),
    )

  private val initialDoctors =
    listOf(
      Doctor(
        id = "doc_jennifer",
        name = "Dr. Jennifer Smith",
        specialtyId = "ortho",
        specialtyName = "Orthopedist",
        experienceYears = 8,
        rating = 4.9,
        reviewCount = 214,
        hospitalName = "St. Jude Orthopedic Center",
        consultationFeeUsd = 65,
        bio =
          "Board-certified orthopedic specialist focusing on sports injuries, foot & ankle rehabilitation, and minimally invasive joint care.",
        avatarUrl = "https://randomuser.me/api/portraits/women/65.jpg",
        avatarRes = R.drawable.img_doc_female_video,
        isOnline = true,
        nextAvailableSlot = "Today, 02:30 PM",
        availableTimeSlots = listOf("09:30 AM", "10:30 AM", "02:30 PM", "04:15 PM"),
        isRecentVisit = false,
        isRecommended = false,
      ),
      Doctor(
        id = "doc_warner",
        name = "Dr. Warner",
        specialtyId = "neuro",
        specialtyName = "Neurology",
        experienceYears = 5,
        rating = 4.8,
        reviewCount = 162,
        hospitalName = "Metro Neuro & Spine Clinic",
        consultationFeeUsd = 75,
        bio =
          "Neurologist specializing in migraine management, nerve conduction diagnostics, and preventive cognitive wellness.",
        avatarUrl = "https://randomuser.me/api/portraits/men/46.jpg",
        avatarRes = R.drawable.img_doc_male_neuro,
        isOnline = true,
        nextAvailableSlot = "Today, 04:00 PM",
        availableTimeSlots = listOf("10:00 AM", "11:30 AM", "04:00 PM", "05:30 PM"),
        isRecentVisit = true,
        isRecommended = false,
      ),
      Doctor(
        id = "doc_roman",
        name = "Dr. Roman Novara",
        specialtyId = "pulmo",
        specialtyName = "Pulmonologist",
        experienceYears = 5,
        rating = 4.9,
        reviewCount = 189,
        hospitalName = "ClearBreath Medical Institute",
        consultationFeeUsd = 70,
        bio =
          "Pulmonary care physician dedicated to asthma control, sleep-related breathing disorders, and respiratory recovery.",
        avatarUrl = "https://randomuser.me/api/portraits/men/75.jpg",
        avatarRes = R.drawable.img_doc_male_pulmo,
        isOnline = true,
        nextAvailableSlot = "Tomorrow, 09:30 AM",
        availableTimeSlots = listOf("09:30 AM", "11:00 AM", "01:30 PM", "03:45 PM"),
        isRecentVisit = true,
        isRecommended = true,
      ),
      Doctor(
        id = "doc_nick",
        name = "Dr. Nick Tylor",
        specialtyId = "podiatry",
        specialtyName = "Podiatrist",
        experienceYears = 7,
        rating = 4.9,
        reviewCount = 148,
        hospitalName = "Stride Care Podiatry",
        consultationFeeUsd = 60,
        bio =
          "Foot and ankle care clinician offering live gait assessments, diabetic foot care, and custom orthotic consultations.",
        avatarUrl = "https://randomuser.me/api/portraits/women/68.jpg",
        avatarRes = R.drawable.img_doc_female_video,
        isOnline = true,
        nextAvailableSlot = "Online Now",
        availableTimeSlots = listOf("10:15 AM", "12:00 PM", "02:00 PM", "04:30 PM"),
        isRecentVisit = false,
        isRecommended = true,
      ),
      Doctor(
        id = "doc_jaffaer",
        name = "Dr. Jaffaer",
        specialtyId = "cardio",
        specialtyName = "Cardiologist",
        experienceYears = 9,
        rating = 4.8,
        reviewCount = 240,
        hospitalName = "HeartCare Specialty Pavilion",
        consultationFeeUsd = 85,
        bio =
          "Preventive cardiologist focused on hypertension management, ECG interpretation, and heart-healthy lifestyle coaching.",
        avatarUrl = "https://randomuser.me/api/portraits/men/52.jpg",
        avatarRes = R.drawable.img_doc_male_pulmo,
        isOnline = true,
        nextAvailableSlot = "Sep 12, 11:00 AM",
        availableTimeSlots = listOf("09:00 AM", "11:00 AM", "03:00 PM"),
        isRecentVisit = false,
        isRecommended = true,
      ),
    )

  private val _homeProfile = MutableStateFlow(initialPatients[0])
  val homeProfile: StateFlow<PatientProfile> = _homeProfile.asStateFlow()

  private val _treatmentPatient = MutableStateFlow(initialPatients[1])
  val treatmentPatient: StateFlow<PatientProfile> = _treatmentPatient.asStateFlow()

  private val _doctors = MutableStateFlow(initialDoctors)
  val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

  private val _appointments =
    MutableStateFlow(
      listOf(
        Appointment(
          id = "apt_1",
          doctorId = "doc_jennifer",
          doctorName = "Dr. Jennifer Smith",
          subtitle = "Orthopedic Consultation (Foot & Ankle)",
          dateLabel = "Wed, 7 Sep 2024",
          timeRange = "10:30 - 11:30 AM",
          type = ConsultationType.CLINIC,
          doctorAvatarUrl = "https://randomuser.me/api/portraits/women/65.jpg",
          doctorAvatarRes = R.drawable.img_doc_female_video,
          liveStatusLabel = "Room Ready",
        )
      )
    )
  val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

  private val _checkupSchedules =
    MutableStateFlow(
      listOf(
        CheckupSchedule(
          id = "chk_1",
          monthLabel = "Sep",
          dayLabel = "07",
          title = "Clinic Visit\nAppointment",
          doctorId = "doc_jennifer",
          doctorShortName = "Dr. Jennifer",
          doctorAvatarUrl = "https://randomuser.me/api/portraits/women/65.jpg",
          doctorAvatarRes = R.drawable.img_doc_female_video,
          type = ConsultationType.CLINIC,
          location = "St. Jude Orthopedic Center, Room 304",
          notes = "Bring previous X-ray scan for ankle mobility follow-up.",
        ),
        CheckupSchedule(
          id = "chk_2",
          monthLabel = "Sep",
          dayLabel = "12",
          title = "Video\nConsulting",
          doctorId = "doc_jaffaer",
          doctorShortName = "Dr. Jaffaer",
          doctorAvatarUrl = "https://randomuser.me/api/portraits/men/52.jpg",
          doctorAvatarRes = R.drawable.img_doc_male_pulmo,
          type = ConsultationType.VIDEO,
          location = "HD Telehealth Video Room",
          notes = "Review weekly blood pressure log and medication response.",
        ),
      )
    )
  val checkupSchedules: StateFlow<List<CheckupSchedule>> = _checkupSchedules.asStateFlow()

  private val _prescriptions =
    MutableStateFlow(
      listOf(
        Prescription(
          id = "rx_1",
          name = "Sumatriptan 50mg",
          dosage = "1 tablet as needed",
          scheduleText = "Morning • After meal",
          prescribedBy = "Dr. Warner",
          remainingRefills = 2,
          isTakenToday = false,
        ),
        Prescription(
          id = "rx_2",
          name = "Albuterol Inhaler",
          dosage = "2 puffs every 6h",
          scheduleText = "08:00 AM & 08:00 PM",
          prescribedBy = "Dr. Roman Novara",
          remainingRefills = 3,
          isTakenToday = true,
        ),
        Prescription(
          id = "rx_3",
          name = "Naproxen Sodium 220mg",
          dosage = "1 capsule",
          scheduleText = "Evening • With water",
          prescribedBy = "Dr. Jennifer Smith",
          remainingRefills = 1,
          isTakenToday = false,
        ),
      )
    )
  val prescriptions: StateFlow<List<Prescription>> = _prescriptions.asStateFlow()

  private val _clinics =
    MutableStateFlow(
      listOf(
        PartnerClinic(
          id = "cl_1",
          name = "St. Jude Orthopedic & Care Center",
          address = "420 Maplewood Ave, Suite 200",
          distanceKm = 1.4,
          liveWaitMinutes = 8,
          isOpenNow = true,
          featuredSpecialties = listOf("Orthopedist", "Podiatrist"),
          rating = 4.9,
        ),
        PartnerClinic(
          id = "cl_2",
          name = "Metro Neuro & Heart Pavilion",
          address = "885 Oakridge Blvd, Downtown",
          distanceKm = 2.8,
          liveWaitMinutes = 14,
          isOpenNow = true,
          featuredSpecialties = listOf("Neurologist", "Cardiologist"),
          rating = 4.8,
        ),
        PartnerClinic(
          id = "cl_3",
          name = "ClearBreath Pulmonary Clinic",
          address = "112 Cedar Park Plaza",
          distanceKm = 4.1,
          liveWaitMinutes = 5,
          isOpenNow = true,
          featuredSpecialties = listOf("Pulmonologist"),
          rating = 4.7,
        ),
      )
    )
  val clinics: StateFlow<List<PartnerClinic>> = _clinics.asStateFlow()

  private val _notifications =
    MutableStateFlow(
      listOf(
        AppNotification(
          id = "not_1",
          title = "Upcoming Appointment",
          body = "Dr. Jennifer Smith confirmed your orthopedic session for Sep 07 at 10:30 AM.",
          timeAgo = "5m ago",
          isUnread = true,
        ),
        AppNotification(
          id = "not_2",
          title = "Dr. Nick Tylor is Online",
          body = "Your podiatry video consultation room is ready whenever you want to connect.",
          timeAgo = "22m ago",
          isUnread = true,
        ),
        AppNotification(
          id = "not_3",
          title = "Treatment Plan Updated",
          body = "Esther Howard reached 65% completion on Post-Sprain Rehabilitation.",
          timeAgo = "2h ago",
          isUnread = false,
        ),
      )
    )
  val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

  private val _liveSyncTick = MutableStateFlow(0L)
  val liveSyncTick: StateFlow<Long> = _liveSyncTick.asStateFlow()

  init {
    // Simulates real-time background sync for clinic wait times, doctor availability & appointment status
    scope.launch {
      var tick = 0L
      while (true) {
        delay(8_000L)
        tick++
        _liveSyncTick.value = tick
        _clinics.update { list ->
          list.mapIndexed { idx, clinic ->
            val delta = if ((tick + idx) % 2L == 0L) 1 else -1
            val nextWait = (clinic.liveWaitMinutes + delta).coerceIn(3, 25)
            clinic.copy(liveWaitMinutes = nextWait)
          }
        }
        _doctors.update { list ->
          list.map { doc ->
            when (doc.id) {
              "doc_jaffaer" -> doc.copy(isOnline = tick % 2L == 1L)
              "doc_warner" ->
                doc.copy(
                  nextAvailableSlot = if (tick % 2L == 0L) "Today, 04:00 PM" else "In 15 mins"
                )
              else -> doc
            }
          }
        }
      }
    }
  }

  fun bookAppointment(
    doctor: Doctor,
    dateLabel: String,
    dayNumber: String,
    monthShort: String,
    timeSlot: String,
    type: ConsultationType,
  ) {
    val newAppointment =
      Appointment(
        id = "apt_${System.currentTimeMillis()}",
        doctorId = doctor.id,
        doctorName = doctor.name,
        subtitle = "${doctor.specialtyName} Consultation (${type.label})",
        dateLabel = dateLabel,
        timeRange = timeSlot,
        type = type,
        doctorAvatarUrl = doctor.avatarUrl,
        doctorAvatarRes = doctor.avatarRes,
        liveStatusLabel = if (type == ConsultationType.VIDEO) "Video Ready" else "Confirmed",
      )
    _appointments.update { current -> listOf(newAppointment) + current }

    val shortName = doctor.name.split(" ").take(2).joinToString(" ")
    val newCheckup =
      CheckupSchedule(
        id = "chk_${System.currentTimeMillis()}",
        monthLabel = monthShort,
        dayLabel = dayNumber,
        title =
          if (type == ConsultationType.CLINIC) "Clinic Visit\nAppointment"
          else "Video\nConsulting",
        doctorId = doctor.id,
        doctorShortName = shortName,
        doctorAvatarUrl = doctor.avatarUrl,
        doctorAvatarRes = doctor.avatarRes,
        type = type,
        location = if (type == ConsultationType.CLINIC) doctor.hospitalName else "HD Telehealth Video Room",
        notes = "Scheduled with ${doctor.name} for $timeSlot.",
      )
    _checkupSchedules.update { current -> listOf(newCheckup) + current }

    _notifications.update { current ->
      listOf(
        AppNotification(
          id = "not_${System.currentTimeMillis()}",
          title = "Appointment Booked",
          body = "${doctor.name} booked for $dateLabel at $timeSlot (${type.label}).",
          timeAgo = "Just now",
          isUnread = true,
        )
      ) + current
    }
  }

  fun togglePrescriptionTaken(prescriptionId: String) {
    var toggledName = ""
    var nowTaken = false
    _prescriptions.update { list ->
      list.map { item ->
        if (item.id == prescriptionId) {
          toggledName = item.name
          nowTaken = !item.isTakenToday
          item.copy(isTakenToday = nowTaken)
        } else {
          item
        }
      }
    }
    if (nowTaken && toggledName.isNotEmpty()) {
      _notifications.update { current ->
        listOf(
          AppNotification(
            id = "not_${System.currentTimeMillis()}",
            title = "Dose Logged",
            body = "Recorded today's dose for $toggledName.",
            timeAgo = "Just now",
            isUnread = true,
          )
        ) + current
      }
    }
  }

  fun requestPrescriptionRefill(prescriptionId: String) {
    _prescriptions.update { list ->
      list.map { item ->
        if (item.id == prescriptionId && item.remainingRefills > 0) {
          item.copy(remainingRefills = item.remainingRefills - 1)
        } else {
          item
        }
      }
    }
    val med = _prescriptions.value.find { it.id == prescriptionId }
    if (med != null) {
      _notifications.update { current ->
        listOf(
          AppNotification(
            id = "not_${System.currentTimeMillis()}",
            title = "Refill Requested",
            body = "Pharmacy refill submitted for ${med.name}.",
            timeAgo = "Just now",
            isUnread = true,
          )
        ) + current
      }
    }
  }

  fun toggleCheckupCompleted(checkupId: String) {
    var completedNow = false
    _checkupSchedules.update { list ->
      list.map { item ->
        if (item.id == checkupId) {
          completedNow = !item.isCompleted
          item.copy(isCompleted = completedNow)
        } else {
          item
        }
      }
    }
    // Real-time feedback: completing a checkup advances the active patient's first treatment plan
    _treatmentPatient.update { patient ->
      val delta = if (completedNow) 0.12f else -0.12f
      val updatedPlans =
        patient.treatmentPlans.mapIndexed { idx, plan ->
          if (idx == 0) {
            plan.copy(progressPercent = (plan.progressPercent + delta).coerceIn(0.1f, 1.0f))
          } else {
            plan
          }
        }
      patient.copy(treatmentPlans = updatedPlans)
    }
  }

  fun cancelAppointment(appointmentId: String) {
    _appointments.update { list -> list.filterNot { it.id == appointmentId } }
  }

  fun markAllNotificationsRead() {
    _notifications.update { list -> list.map { it.copy(isUnread = false) } }
  }

  fun switchTreatmentPatient() {
    val currentId = _treatmentPatient.value.id
    val next = initialPatients.firstOrNull { it.id != currentId } ?: initialPatients.first()
    _treatmentPatient.value = next
  }
}
