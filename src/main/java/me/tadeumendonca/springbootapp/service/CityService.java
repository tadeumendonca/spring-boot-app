package me.tadeumendonca.springbootapp.service;

import me.tadeumendonca.springbootapp.domain.City;
import me.tadeumendonca.springbootapp.exception.BadResourceException;
import me.tadeumendonca.springbootapp.exception.ResourceAlreadyExistsException;
import me.tadeumendonca.springbootapp.exception.ResourceNotFoundException;
import me.tadeumendonca.springbootapp.repository.CityRepository;
import me.tadeumendonca.springbootapp.specification.CitySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    private boolean existsById(Long id) {
        return cityRepository.existsById(id);
    }

    public City findById(Long id) throws ResourceNotFoundException {
        City city = cityRepository.findById(id).orElse(null);
        if (city==null) {
            throw new ResourceNotFoundException("Cannot find City with id: " + id);
        }
        else return city;
    }

    public List<City> findAll(int pageNumber, int rowPerPage) {
        List<City> cities = new ArrayList<>();
        cityRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPage)).forEach(cities::add);
        return cities;
    }

    public List<City> findAllByName(String name, int pageNumber, int rowPerPage) {
        City filter = new City();
        filter.setName(name.toUpperCase());
        Specification<City> spec = new CitySpecification(filter);

        List<City> cities = new ArrayList<>();
        cityRepository.findAll(spec, PageRequest.of(pageNumber - 1, rowPerPage)).forEach(cities::add);
        return cities;
    }

    public List<City> findAllByState(String state, int pageNumber, int rowPerPage) {
        City filter = new City();
        filter.setState(state.toUpperCase());
        Specification<City> spec = new CitySpecification(filter);

        List<City> cities = new ArrayList<>();
        cityRepository.findAll(spec, PageRequest.of(pageNumber - 1, rowPerPage)).forEach(cities::add);
        return cities;
    }

    public City save(City city) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(city.getName())) {
            if (city.getId() != null && existsById(city.getId())) {
                throw new ResourceAlreadyExistsException("City with id: " + city.getId() +
                        " already exists");
            }
            return cityRepository.save(city);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save city");
            exc.addErrorMessage("City is null or empty");
            throw exc;
        }
    }

    public void update(City city)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(city.getName())) {
            if (!existsById(city.getId())) {
                throw new ResourceNotFoundException("Cannot find City with id: " + city.getId());
            }
            cityRepository.save(city);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save contact");
            exc.addErrorMessage("Contact is null or empty");
            throw exc;
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find contact with id: " + id);
        }
        else {
            cityRepository.deleteById(id);
        }
    }

    public Long count() {
        return cityRepository.count();
    }
}
