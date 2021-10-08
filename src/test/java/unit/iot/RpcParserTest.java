package unit.iot;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.rpc.model.schema.*;
import wappsto.iot.rpc.model.schema.incoming.*;
import wappsto.iot.rpc.model.schema.outgoing.*;

import static org.junit.jupiter.api.Assertions.*;

public class RpcParserTest {
    @Nested
    public  class an_incoming_message {

        @Nested
        public class can_be_deserialized_to {
            @Test
            public void a_result() throws Exception {
                RpcIncomingResult result = createResult();

                CommandMock responseCommand = new CommandMock();
                CommandMock controlCommand = new CommandMock();
                CommandMock successResponse = new CommandMock();
                new RpcParser(
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
                RpcStateCommand command = createUpdateCommand();

                CommandMock responseCommand = new CommandMock();
                CommandMock controlCommand = new CommandMock();
                CommandMock successResponse = new CommandMock();
                new RpcParser(
                    responseCommand,
                    controlCommand,
                    successResponse
                ).parse(
                    new ObjectMapper().writeValueAsString(command)
                );

                assertTrue(controlCommand.wasCalled);
            }

            @Test
            @Disabled
            public void a_delete_command() throws Exception {
            }
        }
    }

    private RpcStateCommand createUpdateCommand() {
        RpcStateCommand request =
            new RpcStateCommand();
        request.id = "";
        request.method = Methods.PUT;
        request.params = new RpcCommandParams();
        request.params.data = new RpcCommandData();
        request.params.data.meta = new Meta("thing");
        request.params.data.data = "1";
        return request;
    }

    private RpcIncomingResult createResult() {
        RpcIncomingResult response = new RpcIncomingResult();
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
        public void execute(RpcOutgoingResult response) {
            wasCalled = true;
        }
    }
}
