package ru.job4j.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.domain.Employee;
import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private RestTemplate rest;

    private List<Employee> list;

    EmployeeService() {
        list = Arrays.asList(
                Employee.of(1, "alla", "pugacheva", "123412341234"),
                Employee.of(2, "maxim", "galkin", "432143214321")
        );
    }

    public List<Employee> getAll() {
        return list;
    }
}
