package com.example.foodx_be.service;

import com.example.foodx_be.dto.SearchRequestDTO;
import com.example.foodx_be.ulti.GlobalOperator;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FiltersSpecificationImpl<T> {

    public Specification<T> getSearchSpecification(List<SearchRequestDTO> searchRequestDTOList, GlobalOperator globalOperator) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchRequestDTO requestDTO : searchRequestDTOList) {
                switch (requestDTO.getOperation()) {
                    case EQUAL:
                        if (root.get(requestDTO.getColumn()).getJavaType() == Boolean.class) {
                            predicates.add(criteriaBuilder.equal(root.get(requestDTO.getColumn()), Boolean.valueOf(requestDTO.getValue())));
                        } else {
                            predicates.add(criteriaBuilder.equal(root.get(requestDTO.getColumn()), requestDTO.getValue()));
                        }
                        break;
                    case LIKE:
                        Predicate like = criteriaBuilder.like(root.get(requestDTO.getColumn()), "%" + requestDTO.getValue() + "%");
                        predicates.add(like);
                        break;
                    case IN:
                        String[] splitForIn = requestDTO.getValue().split(", ");
                        Predicate in = root.get(requestDTO.getColumn()).in(Arrays.asList(splitForIn));
                        predicates.add(in);
                        break;
                    case LESS_THAN:
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(requestDTO.getColumn()), requestDTO.getValue());
                        predicates.add(lessThan);
                        break;
                    case GREATER_THAN:
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(requestDTO.getColumn()), requestDTO.getValue());
                        predicates.add(greaterThan);
                        break;
                    case BETWEEN:
                        String[] splitForBetween = requestDTO.getValue().split(", ");
                        Predicate between = criteriaBuilder.between(root.get(requestDTO.getColumn()), Double.parseDouble(splitForBetween[0]), Double.parseDouble(splitForBetween[1]));
                        predicates.add(between);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + requestDTO.getOperation());
                }
            }
            if (globalOperator.equals(GlobalOperator.AND)) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
    }

    public Specification<T> sortByColumn(String column, Sort.Direction direction) {
        return (root, query, criteriaBuilder) -> {
            if (direction == Sort.Direction.ASC) {
                query.orderBy(criteriaBuilder.asc(root.get(column)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(column)));
            }
            return criteriaBuilder.conjunction(); // No additional predicates, just the ordering
        };
    }

    public Specification<T> sortByAverageReview(Sort.Direction direction) {
        return (root, query, criteriaBuilder) -> {
            if (direction == Sort.Direction.ASC) {
                query.orderBy(criteriaBuilder.asc(criteriaBuilder.quot(root.get("reviewSum"), root.get("reviewCount"))));
            } else {
                query.orderBy(criteriaBuilder.desc(criteriaBuilder.quot(root.get("reviewSum"), root.get("reviewCount"))));
            }
            return criteriaBuilder.conjunction(); // No additional predicates, just the ordering
        };
    }
}
