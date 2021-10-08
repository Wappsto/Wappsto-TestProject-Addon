package unit.iot;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.from.server.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonRPCParserTest {
    @Nested
    public  class an_incoming_message {

        @Nested
        public class can_be_deserialized_to {
            @Test
            public void a_result() throws Exception {
                RpcResult result = createResult();

                CommandMock responseCommand = new CommandMock();
                CommandMock controlCommand = new CommandMock();
                CommandMock successResponse = new CommandMock();
                new JsonRPCParser(
                    responseCommand,
                    controlCommand,
                    successResponse
                ).parse(
                    new ObjectMapper().writeValueAsString(result)
                );

                assertTrue(responseCommand.wasCalled);
            }

            @Test
            public void an_update_state_command() throws Exception {
                RpcStateCommand command = createCommand();

                CommandMock responseCommand = new CommandMock();
                CommandMock controlCommand = new CommandMock();
                CommandMock successResponse = new CommandMock();
                new JsonRPCParser(
                    responseCommand,
                    controlCommand,
                    successResponse
                ).parse(
                    new ObjectMapper().writeValueAsString(command)
                );

                assertTrue(controlCommand.wasCalled);
            }

            @Disabled
            @Test
            public void a_delete_command() throws Exception {

            }
        }
    }

    private RpcStateCommand createCommand() {
        RpcStateCommand request =
            new RpcStateCommand();
        request.id = "";
        request.method = Methods.PUT;
        request.params = new JsonRPCRequestFromServerParams();
        request.params.data = new JsonRPCRequestFromServerData();
        request.params.data.meta = new Meta("thing");
        request.params.data.data = "1";
        return request;
    }

    private RpcResult createResult() {
        RpcResult response = new RpcResult();
        response.id = "id";
        response.result = new RpcResultData();
        response.result.value = true;
        return response;
    }

    private class CommandMock implements
        UpdateStateStrategy,
        ResultStrategy,
        SuccessResponseStrategy
    {
        public boolean wasCalled = false;
        @Override
        public void execute(ControlStateData command) {
            wasCalled = true;
        }

        @Override
        public void execute(ResponseData data) {
            wasCalled = true;
        }

        @Override
        public void execute(SuccessResponseToServer response) {
            wasCalled = true;
        }
    }
}
