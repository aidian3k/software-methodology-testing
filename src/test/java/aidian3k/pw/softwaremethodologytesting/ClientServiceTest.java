package aidian3k.pw.softwaremethodologytesting;

import aidian3k.pw.softwaremethodologytesting.dto.ClientCreationDTO;
import aidian3k.pw.softwaremethodologytesting.entity.Client;
import aidian3k.pw.softwaremethodologytesting.infrastructure.exception.ClientNotFoundException;
import aidian3k.pw.softwaremethodologytesting.repository.ClientRepository;
import aidian3k.pw.softwaremethodologytesting.service.ClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	private final Logger log = Logger.getLogger(
		ClientServiceTest.class.getName()
	);

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private ClientService clientService;

	@Test
	void failingTest() {
		assertFalse(true);
	}

	@Test
	@DisplayName("Sample create test")
	void shouldCreateCorrectlyNewClientWhenProvidedDataIsCorrect() {
		// given
		ClientCreationDTO creationDTO = sampleClientCreationDTO();

		// when
		Client sampleClient = Client
			.builder()
			.surname(sampleClientCreationDTO().getSurname())
			.name(sampleClientCreationDTO().getName())
			.email(sampleClientCreationDTO().getEmail())
			.id(1L)
			.build();
		when(clientRepository.save(any())).thenReturn(sampleClient);
		Client client = clientService.createNewClient(creationDTO);
		log.info(
			"Sample create test is done on client with name=" + client.getName()
		);

		// then
		assertAll(
			() -> assertThat(client.getName()).isEqualTo(creationDTO.getName()),
			() -> assertThat(client.getSurname()).isEqualTo(creationDTO.getSurname()),
			() -> assertThat(client.getEmail()).isEqualTo(creationDTO.getEmail())
		);
	}

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/users-creation-parametrized.csv")
	@DisplayName("Sample parametrized test")
	void shouldCorrectlyCreateNewClientWhenTryingToAddMultipleClients(
		String name, String surname, String email
	) {
		// when
		log.info("Starting testing from file");
		ClientCreationDTO clientCreationDTO = ClientCreationDTO
				.builder()
				.name(name)
				.surname(surname)
				.email(email)
				.build();

		Client sampleClient = Client
			.builder()
			.surname(clientCreationDTO.getSurname())
			.name(clientCreationDTO.getName())
			.email(clientCreationDTO.getEmail())
			.id(1L)
			.build();
		when(clientRepository.save(any())).thenReturn(sampleClient);
		Client client = clientService.createNewClient(clientCreationDTO);
		log.info(
			"Sample create test is done on client with name=" + client.getName()
		);

		// then
		assertAll(
			() -> assertThat(client.getName()).isEqualTo(clientCreationDTO.getName()),
			() ->
				assertThat(client.getSurname())
					.isEqualTo(clientCreationDTO.getSurname()),
			() ->
				assertThat(client.getEmail()).isEqualTo(clientCreationDTO.getEmail())
		);
	}

	@ParameterizedTest
	@CsvSource(
		{
			"1, John, Doe, john.doe@example.com",
			"2, Jane, Smith, jane.smith@example.com",
		}
	)
	void shouldCorrectlyFindUsersByIdWhenTryingToFind(
		Long clientId,
		String name,
		String surname,
		String email
	) {
		// Given
		Client client = new Client(
			clientId,
			name,
			surname,
			email,
			new ArrayList<>()
		);

		when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

		// When
		Client retrievedClient = clientService.getClientById(clientId);

		// Then
		verify(clientRepository, times(1)).findById(clientId);
		assertThat(retrievedClient.getName()).isEqualTo(client.getName());
	}

	@ParameterizedTest
	@CsvSource(
		{
			"1, UpdatedName, UpdatedSurname, updated.email@example.com",
			"2, AnotherName, AnotherSurname, another.email@example.com",
		}
	)
	void testUpdateClientById(
		Long clientId,
		String updatedName,
		String updatedSurname,
		String updatedEmail
	) {
		// Given
		ClientCreationDTO updateDTO = new ClientCreationDTO(
			updatedName,
			updatedSurname,
			updatedEmail
		);
		log.info("Performing param test");

		Client currentClient = new Client(
			clientId,
			"John",
			"Doe",
			"john.doe@example.com",
			new ArrayList<>()
		);
		Client updatedClient = currentClient
			.toBuilder()
			.name(updatedName)
			.surname(updatedSurname)
			.email(updatedEmail)
			.build();

		when(clientRepository.save(any())).thenReturn(updatedClient);
		when(clientRepository.findById(any()))
			.thenReturn(Optional.of(currentClient));

		// When
		Client result = clientService.updateClientById(clientId, updateDTO);

		// Then
		verify(clientRepository, times(1)).save(any());
		assertEquals(updatedClient, result);
	}

	@ParameterizedTest
	@CsvSource({ "1", "2" })
	void shouldCorrectlyDeleteClientWhenIdIsOkay(Long clientId) {
		// When
		clientService.deleteClientById(clientId);

		// Then
		verify(clientRepository, times(1)).deleteById(clientId);
	}

	@ParameterizedTest
	@CsvSource({ "1", "2" })
	void shouldThrowAnExceptionWhenIdIsNotCorrect(Long clientId) {
		// Given
		log.info("Stubbing throwing of the void method");
		doThrow(ClientNotFoundException.class)
			.when(clientRepository)
			.deleteById(anyLong());

		// When and Then
		assertThrows(
			ClientNotFoundException.class,
			() -> clientService.deleteClientById(clientId)
		);
	}

	private static ClientCreationDTO sampleClientCreationDTO() {
		return ClientCreationDTO
			.builder()
			.name("adrian")
			.surname("nowosielski")
			.email("adrian@wp.pl")
			.build();
	}
}
