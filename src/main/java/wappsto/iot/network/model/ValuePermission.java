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
    };

    public static ValuePermission from(String string) throws Exception {
        for (ValuePermission v : values()) {
            if (v.toString().equals(string)) return v;
        }
        throw new Exception("Invalid permission");
    }
}
