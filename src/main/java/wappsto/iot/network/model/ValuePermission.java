package wappsto.iot.network.model;

public enum ValuePermission {
    R {
        @Override
        public String toString() {
            return "r";
        }
    },
    W {
        @Override
        public String toString() {
            return "w";
        }
    },
    RW {
        @Override
        public String toString() {
            return "rw";
        }
    }
}
