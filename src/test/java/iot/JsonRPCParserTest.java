package iot;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonRPCParserTest {
    @Nested
    public  class an_incoming_message {

        @Nested
        public class can_be_deserialized_to {
            @Test
            public void a_server_response() throws Exception {
                JsonRPCResponse response = new JsonRPCResponse();
                response.id = "id";
                response.result = new JsonRPCResult();
                response.result.value = true;
                JsonRPCMessage message = new JsonRPCParser().parse(
                    new ObjectMapper().writeValueAsString(response)
                );

                assertTrue(message instanceof JsonRPCResponse);
            }

            @Test
            public void a_server_request() throws Exception {
                JsonRPCRequestFromServer request =
                    new JsonRPCRequestFromServer();
                request.id = "";
                request.method = Methods.PUT;
                request.params = new JsonRPCRequestFromServerParams();
            }
        }
    }


}
