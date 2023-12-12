package sit.project.projectv1.enums;

public enum Role {
    announcer, admin;

    public static boolean contains(String roleName) {
        for (Role role : values()) {
            if (role.name().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
