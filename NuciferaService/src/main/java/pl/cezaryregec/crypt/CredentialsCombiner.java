package pl.cezaryregec.crypt;

public class CredentialsCombiner {
    public String combine(String username, String password, String salt) {
        String result = password;
        int position = 0;
        StringBuilder loopedSaltBuilder = new StringBuilder(salt);
        while (loopedSaltBuilder.length() < username.length()) {
            loopedSaltBuilder.append(salt);
        }
        String loopedSalt = loopedSaltBuilder.toString();

        for (int i = 0; i < username.length(); i++) {
            char character = username.charAt(i);
            character += loopedSalt.charAt(i);

            position += result.length() % password.length();
            while (position >= result.length()) {
                position -= result.length();
            }

            result = result.substring(0, position)
                    + character
                    + result.substring(position);
        }

        return result;
    }
}
