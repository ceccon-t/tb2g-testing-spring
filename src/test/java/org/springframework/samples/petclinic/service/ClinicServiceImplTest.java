package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    PetRepository petRepository;

    @Mock
    VetRepository  vetRepository;

    @Mock
    OwnerRepository  ownerRepository;

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    ClinicServiceImpl  service;

    @Test
    void findPetTypesNoTypes() {
        // given
        given(petRepository.findPetTypes()).willReturn(new ArrayList<>());

        // when
        Collection<PetType> noTypes = service.findPetTypes();

        // then
        assertEquals(0, noTypes.size());
    }

    @Test
    void findPetTypesOneType() {
        // given
        PetType type = new PetType();
        type.setId(1);
        type.setName("Cat");
        given(petRepository.findPetTypes()).willReturn(List.of(type));

        // when
        Collection<PetType> noTypes = service.findPetTypes();

        // then
        assertEquals(1, noTypes.size());
    }

    @Test
    void findPetTypesMultipleTypes() {
        // given
        PetType typeA = new PetType();
        typeA.setId(1);
        typeA.setName("Cat");
        PetType typeB = new PetType();
        typeB.setId(2);
        typeB.setName("Dog");
        given(petRepository.findPetTypes()).willReturn(List.of(typeA, typeB));

        // when
        Collection<PetType> noTypes = service.findPetTypes();

        // then
        assertEquals(2, noTypes.size());
    }
}