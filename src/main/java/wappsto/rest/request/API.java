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
    }
}
