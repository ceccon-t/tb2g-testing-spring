package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



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
    void testNewOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName", "Jimmy")
                .param("lastName", "Buffett")
                .param("address", "123 Duval St")
                .param("city", "Key West")
                .param("telephone", "3151231234"))
            .andExpect(status().is3xxRedirection());
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
    void testFindByNameFindsSeveral() throws Exception {

        Owner johnDoe = new Owner();
        johnDoe.setId(1);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");

        Owner jimDae = new Owner();
        jimDae.setId(2);
        jimDae.setFirstName("Jim");
        jimDae.setLastName("Dae");

        List<Owner> foundOwners = List.of(johnDoe, jimDae);

        given(clinicService.findOwnerByLastName("")).willReturn(foundOwners);

        mockMvc.perform(get("/owners"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("selections"))
            .andExpect(view().name("owners/ownersList"))
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