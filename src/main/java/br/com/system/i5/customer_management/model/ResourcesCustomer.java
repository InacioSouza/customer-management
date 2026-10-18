package br.com.system.i5.customer_management.model;

import br.com.system.i5.customer_management.model.api.AuditInformation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Table(name = "resources_customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ResourcesCustomer extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "number_users")
    private Integer numberUsers;

    @Column(name = "incentivized_users")
    private Integer incentivizedUsers;

    @Column(name = "number_official_channels")
    private Integer numberOfficialChannels;

    @Column(name = "number_unofficial_channels")
    private Integer numberUnofficialChannels;

    @Column(name = "number_instagram_channels")
    private Integer numberInstagramChannels;

    @Column(name = "incentivized_instagram_channels")
    private Integer incentivizedInstagramChannels;

    @Column(name = "official_incentivized_channels")
    private Integer officialIncentivizedChannels;

    @Column(name = "unofficial_incentivized_channels")
    private Integer unofficialIncentivizedChannels;

    @Version
    private Long version;

}
