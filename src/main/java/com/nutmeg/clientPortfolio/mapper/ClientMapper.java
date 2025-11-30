package com.nutmeg.clientPortfolio.mapper;

import com.nutmeg.clientPortfolio.dto.ClientRequestDTO;
import com.nutmeg.clientPortfolio.dto.ClientResponseDTO;
import com.nutmeg.clientPortfolio.dto.HoldingDTO;
import com.nutmeg.clientPortfolio.model.Client;
import com.nutmeg.clientPortfolio.model.Holding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "holding", ignore = true)
    @Mapping(target = "portfolioModel", ignore = true)
    @Mapping(target = "id", ignore = true)
    Client toClient(ClientRequestDTO dto);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "goalName", source = "goal.name")
    @Mapping(target = "goalDate", source = "goal.date")
    @Mapping(target = "riskLevel", source = "portfolioModel.riskLevel")
    ClientResponseDTO toResponseDTO(Client client);

    HoldingDTO toHoldingDTO(Holding holding);
}
