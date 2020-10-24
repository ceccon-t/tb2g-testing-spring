package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                    .param("lastName", "Dont find ME!")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByNameFindsOne() throws Exception {
        Integer ownerId = 1;
        String lastName = "Doe";

        Owner johnDoe = new Owner();
        johnDoe.setId(ownerId);
        johnDoe.setFirstName("John");
        johnDoe.setLastName(lastName);

        List<Owner> foundOwners = List.of(johnDoe);

        given(clinicService.findOwnerByLastName(lastName)).willReturn(foundOwners);

        mockMvc.perform(get("/owners")
                    .param("lastName", lastName)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + ownerId))
        ;

    }

    @Test
    void testOwnerControllerInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
        ;
    }


}