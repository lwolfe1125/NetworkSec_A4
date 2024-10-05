import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class PlayFair {

    //Alphabet for reference, excluding J as I & J are considered the same in Playfair
    public static String alpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Secret Key: ");

        String key = in.nextLine();

        System.out.print("Enter message: ");

        String plainText = in.nextLine();

        ArrayList<ArrayList<Character>> keyMatrix = genKeyMatrix(key);

        String cipher = encrypt(plainText, keyMatrix);
        String decipher = decrypt(cipher, keyMatrix);

        System.out.println("Ciphertext: " + cipher);
        System.out.println("Deciphered: " + decipher);
    }

    public static ArrayList<ArrayList<Character>> genKeyMatrix(String key)
    {
        //Ensure all letters are uppercase
        key = key.toUpperCase();

        //Converting the key into an array of characters
        char[] keyChars = key.toCharArray();

        //Creating an arraylist to hold the characters
        ArrayList<Character> charOrder = new ArrayList<>();

        //For each character in the key
        for(char c : keyChars)
        {
            //If the letter is not already present in the list
            if(!charOrder.contains(c))
            {
                //Add the character to the list
                charOrder.add(c);
            }
        }

        //Ensuring j is not in the list
        if(charOrder.contains('J')) charOrder.remove('J');

        //For the rest of the alphabet
        for(char c : alpha.toCharArray())
        {
            //If the letter is not already present in the list
            if(!charOrder.contains(c))
            {
                //Add the character to the list
                charOrder.add(c);
            }
        }

        //char[][] matrix = new char[5][5];

        ArrayList<ArrayList<Character>> matrix = new ArrayList<>();

        //Turning the list into the matrix
        for(int i = 0; i < 5; i++)
        {
            ArrayList<Character> row = new ArrayList<>();
            for(int j = 0; j < 5; j++)
            {
                row.add(charOrder.remove(0));
            }

            matrix.add(row);
        }

        return matrix;
    }

    public static String encrypt(String plainText, ArrayList<ArrayList<Character>> key)
    {
        String cipherText = "";

        //Removing spaces & punctuation
        plainText = plainText.toUpperCase();
        plainText = plainText.replaceAll("[^A-Z]", "");

        //Convert any J's into I's
        plainText = plainText.replaceAll("J", "I");

        //Sectioning the text
        char[][] pairs = splitText(plainText);

        //For all the pairs of characters
        for(char[] ch : pairs)
        {
            //If the character is equal to 0, we are done
            if(ch[0] == 0) break;

            char charOne = ch[0];
            char charTwo = ch[1];
            int[] indexOne = new int[2];
            int[] indexTwo = new int[2];

            //Find the index of each character
            for(ArrayList<Character> list : key)
            {
                if(list.contains(charOne))
                {
                    indexOne[0] = key.indexOf(list);
                    indexOne[1] = list.indexOf(charOne);
                }

                if(list.contains(charTwo))
                {
                    indexTwo[0] = key.indexOf(list);
                    indexTwo[1] = list.indexOf(charTwo);
                }
            }

            //Each case of the cipher
            if(indexOne[0] == indexTwo[0])
            {
                //If the letters are in the same row, shift right one
                if(indexOne[1] == 4) indexOne[1] = 0;
                else indexOne[1]++;

                if(indexTwo[1] == 4) indexTwo[1] = 0;
                else indexTwo[1]++;
            }

            else if(indexOne[1] == indexTwo[1])
            {
                //If the letters are in the same column, shift down one
                if(indexOne[0] == 4) indexOne[0] = 0;
                else indexOne[0]++;

                if(indexTwo[0] == 4) indexTwo[0] = 0;
                else indexTwo[0]++;
            }

            else
            {
                int col1 = indexOne[1];
                int col2 = indexTwo[1];

                indexOne[1] = col2;
                indexTwo[1] = col1;
            }

            //Translating the indices into letters
            cipherText += key.get(indexOne[0]).get(indexOne[1]);
            cipherText += key.get(indexTwo[0]).get(indexTwo[1]);

            cipherText += " ";
        }

        return cipherText;
    }

    public static String decrypt(String cipher, ArrayList<ArrayList<Character>> key)
    {
        String deciphered = "";

        //Splitting the ciphertext accordingly
        cipher = cipher.replaceAll(" ", "");
        char[][] pairs = splitText(cipher);

        //For all the pairs of characters
        for(char[] ch : pairs)
        {
            //If the character is equal to 0, we are done
            if(ch[0] == 0) break;

            char charOne = ch[0];
            char charTwo = ch[1];
            int[] indexOne = new int[2];
            int[] indexTwo = new int[2];

            //Find the index of each character
            for(ArrayList<Character> list : key)
            {
                if(list.contains(charOne))
                {
                    indexOne[0] = key.indexOf(list);
                    indexOne[1] = list.indexOf(charOne);
                }

                if(list.contains(charTwo))
                {
                    indexTwo[0] = key.indexOf(list);
                    indexTwo[1] = list.indexOf(charTwo);
                }
            }

            //Each case of the cipher
            if(indexOne[0] == indexTwo[0])
            {
                //If the letters are in the same row, shift left one
                if(indexOne[1] == 0) indexOne[1] = 4;
                else indexOne[1]--;

                if(indexTwo[1] == 0) indexTwo[1] = 4;
                else indexTwo[1]--;
            }

            else if(indexOne[1] == indexTwo[1])
            {
                //If the letters are in the same column, shift down one
                if(indexOne[0] == 0) indexOne[0] = 4;
                else indexOne[0]--;

                if(indexTwo[0] == 0) indexTwo[0] = 4;
                else indexTwo[0]--;
            }

            else
            {
                int col1 = indexOne[1];
                int col2 = indexTwo[1];

                indexOne[1] = col2;
                indexTwo[1] = col1;
            }

            //Translating the indices into letters
            deciphered += key.get(indexOne[0]).get(indexOne[1]);
            deciphered += key.get(indexTwo[0]).get(indexTwo[1]);
        }

        return deciphered;
    }

    public static char[][] splitText(String text)
    {
        int pairNum = 0;
        char[][] pairs = new char[text.length()][2];
        for(int i = 0; i < text.length(); i += 2)
        {
            char charOne = text.charAt(i);
            char charTwo;
            //If we are on the last character unevenly
            if(i == (text.length() - 1)) charTwo = 'Z';

            else
            {
                //If the letters are the same, add a padding & reduce i
                if(text.charAt(i+1) == charOne)
                {
                    if(charOne == 'X') charTwo = 'Q';
                    else charTwo = 'X';
                    i--;
                }

                //If the letters are different
                else charTwo = text.charAt(i+1);
            }

            pairs[pairNum][0] = charOne;
            pairs[pairNum][1] = charTwo;

            pairNum++;
        }

        return pairs;
    }
}


