package model;

public enum PostType {
    VIDEO,TEXT,IMAGE,OTHER;
    public static PostType fromString(String type) {
        switch (type.toUpperCase()) {
            case "VIDEO":
                return VIDEO;
            case "TEXT":
                return TEXT;
            case "IMAGE":
                return IMAGE;
            default:
                return OTHER;
        }
    }
    public static PostType fromInt(int type) {
        switch (type) {
            case 1:
                return VIDEO;
            case 2:
                return TEXT;
            case 3:
                return IMAGE;
            default:
                return OTHER;
        }
    }
    public static String getTypeMessage(PostType type) {
        String message;
        switch (type) {
            case VIDEO:
                message = "Ingrese la calidad del video: ";
                break;
            case TEXT:
                message = "Ingrese el texto del post: ";
                break;
            case IMAGE:
                message = "Ingrese las dimensiones de la imagen: ";
                break;
            default:
                message = "Otro";
                break;
        }
        return message;
    }

}
