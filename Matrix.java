import java.util.ArrayList;
import java.util.Scanner;

public class Matrix {

    public static void main(String[] args)
    {
        //Creating a scanner
        Scanner in = new Scanner(System.in);

        //Reading inputs
        System.out.print("Key Length: ");
        int n = in.nextInt();

        System.out.print("Key: ");
        ArrayList<Integer> key = new ArrayList<>();
        for(int i = 0; i < n; i++)
        {
            key.add(in.nextInt());
        }

        in.nextLine();

        System.out.print("Message: ");
        String message = in.nextLine();

        String cipherText = encrypt(key, message);
        System.out.println("Cipher text: " + cipherText);

        System.out.println("Decrypted: " + decrypt(cipherText, key));
    }

    public static String encrypt(ArrayList<Integer> key, String message)
    {
        String cipher = "";

        //Prepping the text
        message = message.replaceAll("[^A-Za-z0-9\s3]", "");
        message = message.replaceAll("[\s]", "%");
        char[] text = message.toCharArray();

        //A matrix for the characters in the message
        int rows = text.length/ key.size();

        if(text.length % key.size() != 0) rows++;



        char[][] matrix = new char[rows][key.size()];

        int index = 0;

        //Lay the characters out in the matrix
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < key.size(); j++)
            {
                //If the message is done, pad with %, otherwise add the next character to the matrix
                if(index >= text.length) matrix[i][j] = '%';
                else matrix[i][j] = text[index];
                index++;
            }
        }

        for(int i : key) {
            for (int j = 0; j < rows; j++) {
                cipher += matrix[j][i-1];
            }
        }

        return cipher;
    }

    public static String decrypt(String cipher, ArrayList<Integer> key)
    {
        String plainText = "";

        //Creating a character array of the text in the cipher
        char[] cipherText = cipher.toCharArray();

        //Determining the number of rows
        int rows = cipherText.length/key.size();

        char[][] matrix = new char[rows][key.size()];

        int index = 0;
        //Placing the ciphertext into the matrix
        for(int i : key)
        {
            for(int j = 0; j < rows; j++)
            {
                matrix[j][i-1] = cipherText[index];
                index++;
            }
        }

        //Converting to plain text
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < key.size(); j++)
            {
                plainText += matrix[i][j];
            }
        }

        //Formatting generated plaintext
        plainText = plainText.replaceAll("\\%", " ");

        return plainText;
    }
}
