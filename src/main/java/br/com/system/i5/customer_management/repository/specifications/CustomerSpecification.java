package br.com.system.i5.customer_management.repository.specifications;

import br.com.system.i5.customer_management.model.BasePricesCustomer;
import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import br.com.system.i5.customer_management.records.filter.CustomerFilter;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> byFilter(CustomerFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            if (filter.listAdministratorId() != null &&
                    !filter.listAdministratorId().isEmpty()) {

                Join<Object, Object> adminCustomerJoin = root.join(
                        "administrator",
                        JoinType.INNER
                );

                predicates.add(
                        adminCustomerJoin.get("administrator")
                                .get("id")
                                .in(filter.listAdministratorId())
                );
            }

            buildsDirectFilters(filter, root, cb, predicates);
            buildsResourceFilters(filter, root, cb, predicates);
            buildsBasePricesFilters(filter, root, cb, predicates);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void buildsBasePricesFilters(
            CustomerFilter filter,
            Root<Customer> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        if (filter.userUnitBasePrice() != null ||
                filter.AIModuleBasePrice() != null ||
                filter.officialWhatsChannelBasePrice() != null ||
                filter.unofficialWhatsChannelBasePrice() != null ||
                filter.instagramChannelBasePrice() != null
        ) {

            Join<Customer, BasePricesCustomer> basePricesJoin = root.join(
                    "basePricesCustomer",
                    JoinType.INNER
            );

            if (filter.userUnitBasePrice() != null) {
                predicates.add(
                        cb.equal(
                                basePricesJoin.get("userUnit"),
                                filter.userUnitBasePrice()
                        )
                );
            }

            if (filter.AIModuleBasePrice() != null) {
                predicates.add(
                        cb.equal(
                                basePricesJoin.get("aiModuleBasePrice"),
                                filter.AIModuleBasePrice()
                        )
                );
            }

            if (filter.officialWhatsChannelBasePrice() != null) {
                predicates.add(
                        cb.equal(
                                basePricesJoin.get("officialWhatsAppChannel"),
                                filter.officialWhatsChannelBasePrice()
                        )
                );
            }

            if (filter.unofficialWhatsChannelBasePrice() != null) {
                predicates.add(
                        cb.equal(
                                basePricesJoin.get("unofficialWhatsAppChannel"),
                                filter.unofficialWhatsChannelBasePrice()
                        )
                );
            }

            if (filter.instagramChannelBasePrice() != null) {
                predicates.add(
                        cb.equal(
                                basePricesJoin.get("instagramChannel"),
                                filter.instagramChannelBasePrice()
                        )
                );
            }

            if (filter.messengerChannelBasePrice() != null) {
                predicates.add(
                        cb.equal(
                                basePricesJoin.get("messengerChannel"),
                                filter.messengerChannelBasePrice()
                        )
                );
            }
        }
    }

    private static void buildsDirectFilters(
            CustomerFilter filter,
            Root<Customer> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        if (filter.alias() != null && !filter.alias().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("alias")),
                    "%" + filter.alias().toLowerCase() + "%"
            ));
        }

        if (filter.registeredBusinessName() != null &&
                !filter.registeredBusinessName().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("registeredBusinessName")),
                    "%" + filter.registeredBusinessName().toLowerCase() + "%"
            ));
        }

        if (filter.tradeName() != null &&
                !filter.tradeName().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("tradeName")),
                    "%" + filter.tradeName().toLowerCase() + "%"
            ));
        }

        if (filter.cnpj() != null && !filter.cnpj().isBlank()) {
            predicates.add(
                    cb.equal(root.get("cnpj"),
                            filter.cnpj())
            );
        }

        if (filter.typeCompany() != null) {
            predicates.add(
                    cb.equal(root.get("typeCompany"),
                            filter.typeCompany())
            );
        }

        if (filter.niche() != null && !filter.niche().isBlank()) {
            predicates.add(
                    cb.equal(
                            root.get("niche"),
                            filter.niche()
                    )
            );
        }

        if (filter.minEntryDate() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("entryDate"),
                            filter.minEntryDate()
                    )
            );
        }
        if (filter.maxEntryDate() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("entryDate"),
                            filter.maxEntryDate()
                    )
            );
        }

        if (filter.minDepartureDate() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("departureDate"),
                            filter.minDepartureDate()
                    )
            );
        }

        if (filter.maxDepartureDate() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("departureDate"),
                            filter.maxDepartureDate()
                    )
            );
        }

        if (filter.hasContract() != null) {
            predicates.add(
                    cb.equal(
                            root.get("hasContract"),
                            filter.hasContract()
                    )
            );
        }

        if (filter.hasLgpdContract() != null) {
            predicates.add(
                    cb.equal(
                            root.get("hasLgpdContract"),
                            filter.hasLgpdContract()
                    )
            );
        }

        if (filter.status() != null) {
            predicates.add(cb.equal(root.get("status"), filter.status()));
        }

        if (filter.minPlatformCost() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("platformCost"),
                            filter.minPlatformCost()
                    )
            );
        }

        if (filter.maxPlatformCost() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("platformCost"),
                            filter.maxPlatformCost()
                    )
            );
        }

        if (filter.minAmountChargedAI() != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            root.get("amountChargedAI"),
                            filter.minAmountChargedAI()
                    )
            );
        }

        if (filter.maxAmountChargedAI() != null) {
            predicates.add(
                    cb.lessThanOrEqualTo(
                            root.get("amountChargedAI"),
                            filter.maxAmountChargedAI()
                    )
            );
        }
    }

    private static void buildsResourceFilters(
            CustomerFilter filter,
            Root<Customer> root,
            CriteriaBuilder cb,
            List<Predicate> predicates
    ) {

        // Users
        if (filter.minQuantityUsers() != null ||
                filter.maxQuantityUsers() != null ||
                filter.minAverageAmountChargedForUser() != null ||
                filter.maxAverageAmountChargedForUser() != null
        ) {

            Join<Customer, ResourcesCustomer> join = root.join(
                    "resourcesCustomers",
                    JoinType.INNER
            );

            predicates.add(
                    cb.equal(
                            join.get("resourceType"),
                            ResourceTypeEnum.USER
                    )
            );

            if (filter.minQuantityUsers() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.minQuantityUsers()
                        )
                );
            }

            if (filter.maxQuantityUsers() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.maxQuantityUsers()
                        )
                );
            }

            if (filter.minAverageAmountChargedForUser() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.minAverageAmountChargedForUser()
                        )
                );
            }

            if (filter.maxAverageAmountChargedForUser() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.maxAverageAmountChargedForUser()
                        )
                );
            }
        }

        // Official WhatsApp Channel
        if (filter.minQuantityOfficialWhatsappChannel() != null ||
                filter.maxQuantityOfficialWhatsappChannel() != null ||
                filter.minAverageAmountChargedForOfficialWhat() != null ||
                filter.maxAverageAmountChargedForOfficialWhat() != null
        ) {

            Join<Customer, ResourcesCustomer> join = root.join(
                    "resourcesCustomers",
                    JoinType.INNER
            );

            predicates.add(
                    cb.equal(
                            join.get("resourceType"),
                            ResourceTypeEnum.OFFICIAL_CHANNEL
                    )
            );

            if (filter.minQuantityOfficialWhatsappChannel() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.minQuantityOfficialWhatsappChannel()
                        )
                );
            }

            if (filter.maxQuantityOfficialWhatsappChannel() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.maxQuantityOfficialWhatsappChannel()
                        )
                );
            }

            if (filter.minAverageAmountChargedForOfficialWhat() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.minAverageAmountChargedForOfficialWhat()
                        )
                );
            }

            if (filter.maxAverageAmountChargedForOfficialWhat() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.maxAverageAmountChargedForOfficialWhat()
                        )
                );
            }
        }

        // Unofficial WhatsApp Channel
        if (filter.minQuantityUnofficialWhatsappChannel() != null ||
                filter.maxQuantityUnofficialWhatsappChannel() != null ||
                filter.minAverageAmountChargedForUnofficialWhats() != null ||
                filter.maxAverageAmountChargedForUnofficialWhats() != null
        ) {
            Join<Customer, ResourcesCustomer> join = root.join(
                    "resourcesCustomers",
                    JoinType.INNER
            );

            predicates.add(
                    cb.equal(
                            join.get("resourceType"),
                            ResourceTypeEnum.UNOFFICIAL_CHANNEL
                    )
            );

            if (filter.minQuantityUnofficialWhatsappChannel() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.minQuantityUnofficialWhatsappChannel()
                        )
                );
            }

            if (filter.maxQuantityUnofficialWhatsappChannel() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.maxQuantityUnofficialWhatsappChannel()
                        )
                );
            }

            if (filter.minAverageAmountChargedForUnofficialWhats() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.minAverageAmountChargedForUnofficialWhats()
                        )
                );
            }

            if (filter.maxAverageAmountChargedForUnofficialWhats() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.maxAverageAmountChargedForUnofficialWhats()
                        )
                );
            }
        }

        // Messenger Channel
        if (filter.minQuantityMessengerChannel() != null ||
                filter.maxQuantityMessengerChannel() != null ||
                filter.minAverageAmountChargedForMessengerChannel() != null ||
                filter.maxAverageAmountChargedForMessengerChannel() != null
        ) {
            Join<Customer, ResourcesCustomer> join = root.join(
                    "resourcesCustomers",
                    JoinType.INNER
            );

            predicates.add(
                    cb.equal(
                            join.get("resourceType"),
                            ResourceTypeEnum.MESSENGER_CHANNEL
                    )
            );

            if (filter.minQuantityMessengerChannel() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.minQuantityMessengerChannel()
                        )
                );
            }

            if (filter.maxQuantityMessengerChannel() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.maxQuantityMessengerChannel()
                        )
                );
            }

            if (filter.minAverageAmountChargedForMessengerChannel() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.minAverageAmountChargedForMessengerChannel()
                        )
                );
            }

            if (filter.maxAverageAmountChargedForMessengerChannel() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.maxAverageAmountChargedForMessengerChannel()
                        )
                );
            }
        }

        // Instagram Channel
        if (filter.minQuantityInstagramChannel() != null ||
                filter.maxQuantityInstagramChannel() != null ||
                filter.minAverageAmountChargedForInstagramChannel() != null ||
                filter.maxAverageAmountChargedForInstagramChannel() != null
        ) {
            Join<Customer, ResourcesCustomer> join = root.join(
                    "resourcesCustomers",
                    JoinType.INNER
            );

            predicates.add(
                    cb.equal(
                            join.get("resourceType"),
                            ResourceTypeEnum.INSTAGRAM_CHANNEL)
            );

            if (filter.minQuantityInstagramChannel() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.minQuantityInstagramChannel()
                        )
                );
            }
            if (filter.maxQuantityInstagramChannel() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("totalQuantity"),
                                filter.maxQuantityInstagramChannel()
                        )
                );
            }
            if (filter.minAverageAmountChargedForInstagramChannel() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.minAverageAmountChargedForInstagramChannel()
                        )
                );
            }

            if (filter.maxAverageAmountChargedForInstagramChannel() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                join.get("averageValueOnlyPaid"),
                                filter.maxAverageAmountChargedForInstagramChannel()
                        )
                );
            }
        }
    }
}


