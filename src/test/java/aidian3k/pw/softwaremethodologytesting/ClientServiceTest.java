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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

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

        // then
        assertAll(
                () -> assertThat(client.getName()).isEqualTo(creationDTO.getName()),
                () -> assertThat(client.getSurname()).isEqualTo(creationDTO.getSurname()),
                () -> assertThat(client.getEmail()).isEqualTo(creationDTO.getEmail())
        );
    }

    @ParameterizedTest
    @MethodSource(value = "provideSampleClientCreationData")
    @DisplayName("Sample parametrized test")
    void shouldCorrectlyCreateNewClientWhenTryingToAddMultipleClients(ClientCreationDTO clientCreationDTO) {
        // when
        Client sampleClient = Client
                .builder()
                .surname(clientCreationDTO.getSurname())
                .name(clientCreationDTO.getName())
                .email(clientCreationDTO.getEmail())
                .id(1L)
                .build();
        when(clientRepository.save(any())).thenReturn(sampleClient);
        Client client = clientService.createNewClient(clientCreationDTO);

        // then
        assertAll(
                () -> assertThat(client.getName()).isEqualTo(clientCreationDTO.getName()),
                () -> assertThat(client.getSurname()).isEqualTo(clientCreationDTO.getSurname()),
                () -> assertThat(client.getEmail()).isEqualTo(clientCreationDTO.getEmail())
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, John, Doe, john.doe@example.com",
            "2, Jane, Smith, jane.smith@example.com",
    })
    void shouldCorrectly(Long clientId, String name, String surname, String email) {
        // Given
        Client client = new Client(clientId, name, surname, email, new ArrayList<>());

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // When
        Client retrievedClient = clientService.getClientById(clientId);

        // Then
        verify(clientRepository, times(1)).findById(clientId);
    }

    @ParameterizedTest
    @CsvSource({
            "1, UpdatedName, UpdatedSurname, updated.email@example.com",
            "2, AnotherName, AnotherSurname, another.email@example.com",
    })
    void testUpdateClientById(Long clientId, String updatedName, String updatedSurname, String updatedEmail) {
        // Given
        ClientCreationDTO updateDTO = new ClientCreationDTO(updatedName, updatedSurname, updatedEmail);

        Client currentClient = new Client(clientId, "John", "Doe", "john.doe@example.com", new ArrayList<>());
        Client updatedClient = currentClient.toBuilder()
                .name(updatedName)
                .surname(updatedSurname)
                .email(updatedEmail)
                .build();

        when(clientRepository.save(any())).thenReturn(updatedClient);
        when(clientRepository.findById(any())).thenReturn(Optional.of(currentClient));

        // When
        Client result = clientService.updateClientById(clientId, updateDTO);

        // Then
        verify(clientRepository, times(1)).save(any());
        assertEquals(updatedClient, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
    })
    void shouldCorrectlyDeleteClientWhenIdIsOkay(Long clientId) {
        // When
        clientService.deleteClientById(clientId);

        // Then
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
    })
    void shouldThrowAnExceptionWhenIdIsNotCorrect(Long clientId) {
        // Given
        doThrow(ClientNotFoundException.class).when(clientRepository).deleteById(anyLong());

        // When and Then
        assertThrows(ClientNotFoundException.class, () -> {
            clientService.deleteClientById(clientId);
        });
    }

    private static Stream<Arguments> provideSampleClientCreationData() {
        return Stream.of(
                Arguments.of(sampleClientCreationDTO().toBuilder().name("cos").build()),
                Arguments.of(sampleClientCreationDTO().toBuilder().surname("niecos").build()),
                Arguments.of(sampleClientCreationDTO().toBuilder().email("some-email").build())
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
