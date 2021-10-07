package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import extensions.mocks.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.*;
import wappsto.iot.network.model.*;
import wappsto.iot.ssl.model.*;
import wappsto.network.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(DataStoreInjector.class)
@ExtendWith(CreatorResponseInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(ConnectionInjector.class)
public class LoadAndRunSavedClientControllerTest {
    @Test
    public void fails_to_load_if_network_does_not_exist(
        DataStore store,
        Connection connection
    ) {
        assertThrows(
            FailureException.class,
            () -> new LoadAndRunSavedClient.Controller(
                store,
                UUID.randomUUID().toString(),
                connection
            ).execute()
        );
    }

    @Test
    public void loads_network_from_data_store(
        DataStore store,
        CreatorResponse creator,
        NetworkSchema schema,
        Connection connection
    )
        throws Exception
    {
        String savedNetworkId = saveNetworkToStore(store, creator, schema);
        String loadedNetworkId = new LoadAndRunSavedClient
            .Controller(store, savedNetworkId, connection).execute();
        assertEquals(savedNetworkId, loadedNetworkId);
    }

    @Test
    public void starts_new_client_with_existing_network(
        DataStore store,
        CreatorResponse creator,
        NetworkSchema schema,
        Connection connection
    )
        throws Exception
    {
        String networkId = saveNetworkToStore(store, creator, schema);
        new LoadAndRunSavedClient.Controller(
            store,
            networkId,
            connection
        ).execute();
        String sent = ((InMemoryConnection) connection).lastReceived;
        assertTrue(sent.contains(networkId));
    }

    private String saveNetworkToStore(
        DataStore store,
        CreatorResponse creator,
        NetworkSchema schema
    ) {
        String savedNetworkId = creator.network.id;
        schema.meta.id = UUID.fromString(savedNetworkId);
        store.save(
            savedNetworkId,
            new NetworkInstance(new WappstoCerts(creator), schema)
        );
        return savedNetworkId;
    }
}
