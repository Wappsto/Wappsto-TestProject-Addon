package wappsto.iot.rpc;

public class RpcStrategies {
    public final ResultStrategy result;
    public final UpdateStateStrategy updateState;
    public final SuccessResponseStrategy successResponse;
    public final DeleteStrategy delete;

    public RpcStrategies(
        ResultStrategy result,
        UpdateStateStrategy updateState,
        SuccessResponseStrategy successResponse,
        DeleteStrategy delete) {
        this.result = result;
        this.updateState = updateState;
        this.successResponse = successResponse;
        this.delete = delete;
    }
}
