package me.tadeumendonca.springbootapp.repository;

import me.tadeumendonca.springbootapp.domain.City;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CityRepository extends PagingAndSortingRepository<City, Long>,
        JpaSpecificationExecutor<City> {
}
