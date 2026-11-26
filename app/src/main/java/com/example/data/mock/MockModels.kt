package com.example.data.mock

import androidx.annotation.DrawableRes

enum class SpecialtyIconType {
  NEUROLOGY,
  CARDIOLOGY,
  ORTHOPEDICS,
  PULMONOLOGY,
  PODIATRY,
}

enum class ConsultationType(val label: String) {
  CLINIC("Clinic Visit"),
  VIDEO("Video Consulting"),
}

data class SpecialtyCategory(
  val id: String,
  val name: String,
  val iconType: SpecialtyIconType,
  val accentColorHex: Long,
)

data class Doctor(
  val id: String,
  val name: String,
  val specialtyId: String,
  val specialtyName: String,
  val experienceYears: Int,
  val rating: Double,
  val reviewCount: Int,
  val hospitalName: String,
  val consultationFeeUsd: Int,
  val bio: String,
  @DrawableRes val avatarRes: Int,
  val isOnline: Boolean,
  val nextAvailableSlot: String,
  val availableTimeSlots: List<String>,
  val isRecentVisit: Boolean = false,
  val isRecommended: Boolean = false,
)

data class Appointment(
  val id: String,
  val doctorId: String,
  val doctorName: String,
  val subtitle: String,
  val dateLabel: String,
  val timeRange: String,
  val type: ConsultationType,
  @DrawableRes val doctorAvatarRes: Int,
)

data class CheckupSchedule(
  val id: String,
  val monthLabel: String,
  val dayLabel: String,
  val title: String,
  val doctorId: String,
  val doctorShortName: String,
  @DrawableRes val doctorAvatarRes: Int,
  val type: ConsultationType,
  val location: String,
  val notes: String,
  val isCompleted: Boolean = false,
)

data class TreatmentPlan(
  val id: String,
  val title: String,
  val doctorName: String,
  val progressPercent: Float,
  val nextMilestone: String,
  val durationText: String,
)

data class PatientProfile(
  val id: String,
  val displayName: String,
  val firstName: String,
  val genderAgeLabel: String,
  val heightLabel: String,
  val bloodType: String,
  val isPro: Boolean,
  @DrawableRes val avatarRes: Int,
  val treatmentPlans: List<TreatmentPlan>,
)

data class Prescription(
  val id: String,
  val name: String,
  val dosage: String,
  val scheduleText: String,
  val prescribedBy: String,
  val remainingRefills: Int,
  val isTakenToday: Boolean,
)

data class PartnerClinic(
  val id: String,
  val name: String,
  val address: String,
  val distanceKm: Double,
  val liveWaitMinutes: Int,
  val isOpenNow: Boolean,
  val featuredSpecialties: List<String>,
  val rating: Double,
)

data class AppNotification(
  val id: String,
  val title: String,
  val body: String,
  val timeAgo: String,
  val isUnread: Boolean,
)
