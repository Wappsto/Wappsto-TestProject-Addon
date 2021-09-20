package wappsto.rest.request;

public enum API {
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

    CREATOR {
        @Override
        public String toString() {
            return "creator";
        }
    }
}
