package me.tadeumendonca.springbootapp.specification;

import me.tadeumendonca.springbootapp.domain.City;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CitySpecification implements Specification<City> {

    private City filter;

    public CitySpecification(City filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<City> root, CriteriaQuery<?> cq,
                                 CriteriaBuilder cb) {

        Predicate p = cb.disjunction();

        if (filter.getId() != null) {
            p.getExpressions().add(cb.equal(root.get("id"), filter.getId()));
        }

        if (filter.getName() != null) {
            p.getExpressions().add(cb.like(root.get("name"), "%" + filter.getName() .toUpperCase()+ "%"));
        }

        if (filter.getState() != null) {
            p.getExpressions().add(cb.like(root.get("state"), "%" + filter.getState().toUpperCase() + "%"));
        }

        return p;
    }
}
