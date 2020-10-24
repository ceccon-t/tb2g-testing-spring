package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    public static final String VET_LIST_ROUTE = "vets/vetList";

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController vetController;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(vetController).build();
    }

    @Test
    void testControllerShowVetList() throws Exception {
        mockMvc.perform(get("/vets.html"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vets"))
            .andExpect(view().name("vets/vetList"))
        ;
    }

    @Test
    void showVetListNoVets() {
        // given
        Map<String, Object> model = new HashMap<>();
        given(clinicService.findVets()).willReturn(new ArrayList<>());

        // when
        String route = vetController.showVetList(model);

        // then
        then(clinicService).should(atLeastOnce()).findVets();
        assertEquals(VET_LIST_ROUTE, route);
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
        assertEquals(VET_LIST_ROUTE, route);
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
        assertEquals(VET_LIST_ROUTE, route);
        assertTrue(model.containsKey("vets"));
        assertEquals(2, ((Vets) model.get("vets")).getVetList().size());
    }

    @Test
    void showResourcesVetListNoVet() {
        // given
        given(clinicService.findVets()).willReturn(new ArrayList<>());

        // when
        Vets noVets = vetController.showResourcesVetList();

        // then
        assertEquals(0, noVets.getVetList().size());
    }

    @Test
    void showResourcesVetListOneVet() {
        // given
        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("John");
        vet.setLastName("Doe");
        given(clinicService.findVets()).willReturn(List.of(vet));

        // when
        Vets vets = vetController.showResourcesVetList();

        // then
        assertEquals(1, vets.getVetList().size());
    }

    @Test
    void showResourcesVetListMultipleVets() {
        // given
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
        Vets vets = vetController.showResourcesVetList();

        // then
        assertEquals(2, vets.getVetList().size());
    }
}