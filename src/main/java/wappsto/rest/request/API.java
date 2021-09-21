package wappsto.rest.request;

public enum API {
    _2_1 {
        @Override
        public String toString() {
            return "2.1";
        }
    },

    _2_0 {
        @Override
        public String toString() {
            return "2.0";
        }
    },

    REGISTER {
        @Override
        public String toString() {
            return "register";
        }
    },
    USER {
        @Override
        public String toString() {
            return "user";
        }
    },
    SESSION {
        @Override
        public String toString() {
            return "session";
        }
    },

    NETWORK {
        @Override
        public String toString() {
            return "network";
        }
    },

    INSTALLATION {
        @Override
        public String toString() {
            return "installation";
        }
    },
    MARKET {
        @Override
        public String toString() {
            return "search/market";
        }
    },

    ACL {
        @Override
        public String toString() {
            return "acl";
        }
    },

    CREATOR {
        @Override
        public String toString() {
            return "creator";
        }
    }
}
