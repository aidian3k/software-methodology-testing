package aidian3k.pw.softwaremethodologytesting.service;

import aidian3k.pw.softwaremethodologytesting.dto.ClientCreationDTO;
import aidian3k.pw.softwaremethodologytesting.entity.Client;
import aidian3k.pw.softwaremethodologytesting.infrastructure.exception.ClientNotFoundException;
import aidian3k.pw.softwaremethodologytesting.repository.ClientRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

	private final ClientRepository clientRepository;

	public Client createNewClient(ClientCreationDTO creationDTO) {
		Client newClient = Client
			.builder()
			.email(creationDTO.getEmail())
			.name(creationDTO.getName())
			.surname(creationDTO.getSurname())
			.build();

		return clientRepository.save(newClient);
	}

	public List<Client> getAllClients() {
		return clientRepository.findAll();
	}

	public Client getClientById(Long clientId) {
		return clientRepository
			.findById(clientId)
			.orElseThrow(() ->
				new ClientNotFoundException(
					String.format("Client with id=[%d] could not be found", clientId)
				)
			);
	}

	public Client updateClientById(
		Long clientId,
		ClientCreationDTO clientCreationDTO
	) {
		Client currentClient = getClientById(clientId);
		Client updatedClient = currentClient
			.toBuilder()
			.name(clientCreationDTO.getName())
			.surname(clientCreationDTO.getSurname())
			.email(clientCreationDTO.getEmail())
			.build();

		return clientRepository.save(updatedClient);
	}

	public Client updateClient(Client updatedClient) {
		return clientRepository.save(updatedClient);
	}
}
