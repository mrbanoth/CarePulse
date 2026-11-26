package com.example.data.repository

import com.example.data.mock.AppNotification
import com.example.data.mock.Appointment
import com.example.data.mock.CheckupSchedule
import com.example.data.mock.ConsultationType
import com.example.data.mock.Doctor
import com.example.data.mock.MockRealTimeDataSource
import com.example.data.mock.PartnerClinic
import com.example.data.mock.PatientProfile
import com.example.data.mock.Prescription
import com.example.data.mock.SpecialtyCategory
import kotlinx.coroutines.flow.StateFlow

class DoctorRepository(
  private val dataSource: MockRealTimeDataSource = MockRealTimeDataSource
) {
  val specialties: List<SpecialtyCategory> = dataSource.specialties
  val homeProfile: StateFlow<PatientProfile> = dataSource.homeProfile
  val treatmentPatient: StateFlow<PatientProfile> = dataSource.treatmentPatient
  val doctors: StateFlow<List<Doctor>> = dataSource.doctors
  val appointments: StateFlow<List<Appointment>> = dataSource.appointments
  val checkupSchedules: StateFlow<List<CheckupSchedule>> = dataSource.checkupSchedules
  val prescriptions: StateFlow<List<Prescription>> = dataSource.prescriptions
  val clinics: StateFlow<List<PartnerClinic>> = dataSource.clinics
  val notifications: StateFlow<List<AppNotification>> = dataSource.notifications
  val liveSyncTick: StateFlow<Long> = dataSource.liveSyncTick

  fun bookAppointment(
    doctor: Doctor,
    dateLabel: String,
    dayNumber: String,
    monthShort: String,
    timeSlot: String,
    type: ConsultationType,
  ) {
    dataSource.bookAppointment(doctor, dateLabel, dayNumber, monthShort, timeSlot, type)
  }

  fun togglePrescriptionTaken(id: String) = dataSource.togglePrescriptionTaken(id)

  fun requestPrescriptionRefill(id: String) = dataSource.requestPrescriptionRefill(id)

  fun toggleCheckupCompleted(id: String) = dataSource.toggleCheckupCompleted(id)

  fun cancelAppointment(id: String) = dataSource.cancelAppointment(id)

  fun markAllNotificationsRead() = dataSource.markAllNotificationsRead()

  fun switchTreatmentPatient() = dataSource.switchTreatmentPatient()
}
