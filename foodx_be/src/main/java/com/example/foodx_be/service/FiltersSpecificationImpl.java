package com.example.foodx_be.service;

import com.example.foodx_be.dto.SearchRequestDTO;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.RestaurantTag;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.ulti.GlobalOperator;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FiltersSpecificationImpl<T> {

    public Specification<T> getSearchSpecification(List<SearchRequestDTO> searchRequestDTOList, GlobalOperator operator) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchRequestDTO requestDTO : searchRequestDTOList) {
                switch (requestDTO.getOperation()) {
                    case EQUAL:
                        if (root.get(requestDTO.getColumn()).getJavaType() == Boolean.class) {
                            predicates.add(criteriaBuilder.equal(root.get(requestDTO.getColumn()), Boolean.valueOf(requestDTO.getValue())));
                        } else if (root.get(requestDTO.getColumn()).getJavaType() == UUID.class) {
                            try {
                                UUID uuid = UUID.fromString(requestDTO.getValue());
                                predicates.add(criteriaBuilder.equal(root.get(requestDTO.getColumn()), uuid));
                            } catch (IllegalArgumentException e) {
                                // Handle the case where the value is not a valid UUID string
                                // You can either log an error or rethrow the exception
                                throw new RuntimeException("Invalid UUID string: " + requestDTO.getValue(), e);
                            }
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
                        Predicate between = criteriaBuilder.between(root.get(requestDTO.getColumn()), new BigDecimal(Double.parseDouble(splitForBetween[0])), new BigDecimal(Double.parseDouble(splitForBetween[1])));
                        predicates.add(between);
                        break;
                    case TAG_IN:
                        // Trường hợp tìm kiếm theo danh sách ID Tag
                        String[] tagIds = requestDTO.getValue().split(", ");
                        List<UUID> uuidList = new ArrayList<>();
                        for (String tagId : tagIds) {
                            uuidList.add(UUID.fromString(tagId));
                        }
                        Join<Restaurant, RestaurantTag> restaurantTagJoin = root.join("restaurantTagList"); // Join từ thực thể Restaurant tới RestaurantTag
                        Join<RestaurantTag, Tag> tagJoin = restaurantTagJoin.join("tag"); // Join từ RestaurantTag tới Tag
                        Predicate tagInPredicate = tagJoin.get("id").in(uuidList);
                        predicates.add(tagInPredicate);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + requestDTO.getOperation());
                }
            }
            if (operator.equals(GlobalOperator.AND)) {
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
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> sortByAverageReview(Sort.Direction direction) {
        return (root, query, criteriaBuilder) -> {
            if (direction == Sort.Direction.ASC) {
                query.orderBy(criteriaBuilder.asc(criteriaBuilder.quot(root.get("reviewSum"), root.get("reviewCount"))));
            } else {
                query.orderBy(criteriaBuilder.desc(criteriaBuilder.quot(root.get("reviewSum"), root.get("reviewCount"))));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
