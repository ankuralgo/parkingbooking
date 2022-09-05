package org.ankur.parkinglot.service;

import org.ankur.parkinglot.dto.ParkingMetaDTO;

import javax.transaction.Transactional;

public interface OnboardingService {
    @Transactional
    long onboardParking(ParkingMetaDTO parking);
}
