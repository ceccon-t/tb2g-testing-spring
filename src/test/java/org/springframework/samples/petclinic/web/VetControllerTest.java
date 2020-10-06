package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController vetController;

    @Test
    void showVetListNoVets() {
        // given
        Map<String, Object> model = new HashMap<>();
        given(clinicService.findVets()).willReturn(new ArrayList<>());

        // when
        String route = vetController.showVetList(model);

        // then
        then(clinicService).should(atLeastOnce()).findVets();
        assertEquals("vets/vetList", route);
        assertTrue(model.containsKey("vets"));
        assertEquals(0, ((Vets) model.get("vets")).getVetList().size());
    }

    @Test
    void showVetListNoVetsOneVet() {
        // given
        Map<String, Object> model = new HashMap<>();
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");
        given(clinicService.findVets()).willReturn(List.of(vet));

        // when
        String route = vetController.showVetList(model);

        // then
        then(clinicService).should(atLeastOnce()).findVets();
        assertEquals("vets/vetList", route);
        assertTrue(model.containsKey("vets"));
        assertEquals(1, ((Vets) model.get("vets")).getVetList().size());
    }

    @Test
    void showVetListNoVetsMultipleVets() {
        // given
        Map<String, Object> model = new HashMap<>();
        Vet vetA = new Vet();
        vetA.setId(1);
        vetA.setFirstName("John");
        vetA.setLastName("Doe");
        Vet vetB = new Vet();
        vetB.setId(2);
        vetB.setFirstName("James");
        vetB.setLastName("Dean");
        given(clinicService.findVets()).willReturn(List.of(vetA, vetB));

        // when
        String route = vetController.showVetList(model);

        // then
        then(clinicService).should(atLeastOnce()).findVets();
        assertEquals("vets/vetList", route);
        assertTrue(model.containsKey("vets"));
        assertEquals(2, ((Vets) model.get("vets")).getVetList().size());
    }

    @Test
    void showResourcesVetList() {
    }
}