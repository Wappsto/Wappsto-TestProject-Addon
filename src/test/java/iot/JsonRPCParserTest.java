package iot;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.Command;
import wappsto.iot.rpc.model.from.server.*;

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

                CommandMock responseCommand = new CommandMock();
                CommandMock controlCommand = new CommandMock();
                new JsonRPCParser(responseCommand, controlCommand).parse(
                    new ObjectMapper().writeValueAsString(response)
                );

                assertTrue(responseCommand.wasCalled);
            }

            @Test
            public void a_server_request() throws Exception {
                JsonRPCRequestFromServer request =
                    new JsonRPCRequestFromServer();
                request.id = "";
                request.method = Methods.PUT;
                request.params = new JsonRPCRequestFromServerParams();

                CommandMock responseCommand = new CommandMock();
                CommandMock controlCommand = new CommandMock();
                new JsonRPCParser(responseCommand, controlCommand).parse(
                    new ObjectMapper().writeValueAsString(request)
                );

                assertTrue(controlCommand.wasCalled);
            }
        }
    }

    private class CommandMock implements Command {
        public boolean wasCalled = false;
        @Override
        public void execute(JsonRpcMessage command) {
            wasCalled = true;
        }
    }
}
