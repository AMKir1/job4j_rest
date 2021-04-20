package ru.job4j.auth.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.domain.Person;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonController personController;

    @Test
    public void whenGetAll() throws Exception {
        Person p1 = Person.of(1, "parsentev", "123");
        Person p2 = Person.of(2, "ban", "123");
        Person p3 = Person.of(3, "ivan", "123");
        when(personController.getAll()).thenReturn(Arrays.asList(p1, p2, p3));
        mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].login", is("parsentev")))
                .andExpect(jsonPath("$[0].password", is("123")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].login", is("ban")))
                .andExpect(jsonPath("$[1].password", is("123")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].login", is("ivan")))
                .andExpect(jsonPath("$[2].password", is("123")));
        verify(personController, times(1)).getAll();
        verifyNoMoreInteractions(personController);

    }

    @Test
    public void whenAdd() throws Exception {
        Person p = Person.of(4, "newUser", "123");
        when(personController.create(any(Person.class))).thenReturn(new ResponseEntity<Person>(p, HttpStatus.OK));
        mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"newUser\",\"password\":\"123\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(personController, times(1)).create(any(Person.class));
        ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
        verify(personController).create(arg.capture());
        assertEquals(arg.getValue().getLogin(), "newUser");
        assertEquals(arg.getValue().getPassword(), "123");
    }

    @Test
    public void whenUpdate() throws Exception {
        when(personController.update(any(Person.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"ivan\",\"password\":\"AAA\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(personController, times(1)).update(any(Person.class));
        ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
        verify(personController).update(arg.capture());
        assertEquals("ivan", arg.getValue().getLogin());
        assertEquals("AAA", arg.getValue().getPassword());
    }

    @Test
    public void whenFindById() throws Exception {
        Person p = Person.of(3, "ivan", "123");
        when(personController.findById(anyInt())).thenReturn(new ResponseEntity<>(p, HttpStatus.OK));
        mockMvc.perform(get("/person/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.login", is("ivan")))
                .andExpect(jsonPath("$.password", is("123")));
        verify(personController, times(1)).findById(anyInt());
        verifyNoMoreInteractions(personController);
    }

    @Test
    public void whenDelete() throws Exception {
        when(personController.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/person/3"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(personController, times(1)).delete(anyInt());
        verifyNoMoreInteractions(personController);
    }

}
