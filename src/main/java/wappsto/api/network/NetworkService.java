package wappsto.api.network;

import wappsto.api.network.model.*;
import wappsto.api.session.model.*;

import java.util.*;

public interface NetworkService {
    void claim(String id) throws Exception;

    CreatorResponse getCreator() throws Exception;

    CreatorResponse getCreator(boolean manufacturerAsOwner) throws Exception;

    NetworkMeta createNetwork() throws Exception;

    NetworkMeta fetch(String id) throws Exception;

    void share(NetworkMeta network, UserResponse friend) throws Exception;

    void share(String networkId, String friendUsername) throws Exception;

    void updateState(UUID id, String data) throws Exception;

    String getState(UUID id) throws Exception;

    String createDevice(String networkId) throws Exception;

    List<UUID> getDevice(UUID device);
}
