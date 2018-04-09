package swing;

public class Sentence {
	
	public static final char unicodeAlef = 1488;
	public static final char unicodeTaf = 1514;
	public static final String ERROR = "ישנם תווים לא מורשים!";
	public static final char[] unicodeSuffix = {1498, 1501, 1503, 1507, 1509};
	public static final char[] unicodeAllowed = {0x3 ,0x22, 10,'\n',' ', '.', '!', '_', ',', '+', '-', '*', '/', 1498, 1501, 1503, 1507, 1509};
	
	public static final char[] letters = {1488, 1489, 1490, 1491, 1492, 1493, 1494, 1495, 1496, 1497, 
										  1499, 1500 ,1502, 1504, 1505, 1506, 1508, 1510, 1511, 1512,
										  1513,1514 };
	
	
	public static int findIndexLetter(char c){
		int low = 0;
        int high = letters.length - 1;
         
        while(high >= low) 
        {
            int middle = (low + high) / 2;
            if(letters[middle] == c) {
                return middle;
            }
            if(letters[middle] < c) {
                low = middle + 1;
            }
            if(letters[middle] > c) {
                high = middle - 1;
            }
        }
        return -1;
	}
	
	public static char getCharATBSH(char c){
		if(isSuffinx(c))
			c++;
		int i = findIndexLetter(c);
		if(i == -1)
			return c;
		
		return letters[(letters.length-1) - i];
	}
	
	public static char getCharALBAM(char c){
		if(isSuffinx(c))
			c++;
		int i = findIndexLetter(c);
		if(i == -1)
			return c;
		
		return letters[((letters.length/2) + i) % letters.length];
	}
	
	
	public static char getCharShift(char c, int s){
		if(isSuffinx(c))
			c++;
		int i = findIndexLetter(c);
		if(i == -1)
			return c;
		
		int res = i + s;
		if(s < 0)
			res = letters.length + s + i;		
		if(c >= unicodeAlef && c <= unicodeTaf && !isSuffinx(c))
			return letters[res % letters.length];
		else
			return c;
	}
	
	
	// check if the char is one of the allowed letters
	public static boolean isAllowedLetters(char c){
		for(char suf: unicodeAllowed){
			if(c == suf)
				return true;
		}
		return false;
	}
	
	// check if the char is some suffix in hebrew
	private static boolean isSuffinx(char c){
		for(char suf: unicodeSuffix){
			if(c == suf)
				return true;
		}
		return false;
	}
	
	
	
	private static String changeLettersToSuffix(String string){
		String str = "";
		for(int i = 0; i < string.length() ; i++)
		{
			if(string.charAt(i) >= unicodeAlef &&
			   string.charAt(i) <= unicodeTaf  && 
			   isSuffinx((char)((int)(string.charAt(i)) - 1)) &&
			   ((i < string.length()-1 && string.charAt(i + 1) == ' ') || (i == string.length()-1)))
			{
				char ch = string.charAt(i);
				ch--;
				str += ch;
			}
			else 
			{
				str += string.charAt(i);
			}
		}
		return str;
	}
	
	// change all the suffix letter to nomal hebrew letters
	private static String changeAllSuffixLetters(String string){
		String str = "";
		for(int i = 0 ; i < string.length() ; i++)
		{
			if(string.charAt(i) >= unicodeAlef && string.charAt(i) <= unicodeTaf)
			{
				char ch =  getCharShift((char)(string.charAt(i)) , 0);
				if(isSuffinx(ch)){
					ch++;
					str += ch;
				}
				else
					str += ch;
			}
			else if(isAllowedLetters((char)(string.charAt(i))))
			{
				str += string.charAt(i);
			}
			else{
				return "";
			}
		}
		return str;
	}
	
	// calculate the sentence after shift 
	public static String calculateShiftStatment(String sentence, int shift){
		String str = sentence;
		
		sentence = changeAllSuffixLetters(sentence);
		if(sentence.equals("")){
			sentence = str;
			return ERROR;
		}
		
		str = "";
		for(int i = 0; i < sentence.length(); i++)
		{
			if((sentence.charAt(i) >= unicodeAlef && sentence.charAt(i) <= unicodeTaf))
			{
				str += getCharShift(sentence.charAt(i) , shift);
			}
			else if(isAllowedLetters(sentence.charAt(i)))
			{
				str += sentence.charAt(i);
			}
		}
		return changeLettersToSuffix(str);
	}
	
	// calculate the sentence after shift 
	public static String calculateATBASHStatment(String sentence){
		String str = sentence;
		
		sentence = changeAllSuffixLetters(sentence);
		if(sentence.equals("")){
			sentence = str;
			return "The sentence contain illigal letters!";
		}
		
		str = "";
		for(int i = 0; i < sentence.length(); i++)
		{
			if((sentence.charAt(i) >= unicodeAlef && sentence.charAt(i) <= unicodeTaf))
			{
				str += getCharATBSH(sentence.charAt(i));
			}
			else if(isAllowedLetters(sentence.charAt(i)))
			{
				str += sentence.charAt(i);
			}
		}
		return changeLettersToSuffix(str);
	}
	
	// calculate the sentence after shift 
		public static String calculateALBAMStatment(String sentence){
			String str = sentence;
			
			sentence = changeAllSuffixLetters(sentence);
			if(sentence.equals("")){
				sentence = str;
				return "The sentence contain illigal letters!";
			}
			
			str = "";
			for(int i = 0; i < sentence.length(); i++)
			{
				if((sentence.charAt(i) >= unicodeAlef && sentence.charAt(i) <= unicodeTaf))
				{
					str += getCharALBAM(sentence.charAt(i));
				}
				else if(isAllowedLetters(sentence.charAt(i)))
				{
					str += sentence.charAt(i);
				}
			}
			return changeLettersToSuffix(str);
		}
}
