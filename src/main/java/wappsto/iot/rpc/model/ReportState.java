package wappsto.iot.rpc.model;

import java.util.*;

public class ReportState extends Params {

    public ReportState(UUID state, ReportData data) {
        super("/state/" + state.toString(), data);
    }
}