public class Arithmetic_Operations {
    
    int answer;


    public int add(String Num1 , String Num2){
        int a = Integer.parseInt(Num1);
        int b = Integer.parseInt(Num2);
        answer= a+b;
        return answer;
    }
    public int Minus(String Num1 , String Num2){
        int a = Integer.parseInt(Num1);
        int b = Integer.parseInt(Num2);
        answer= a-b;
        return answer;
    }
    public int Multiple(String Num1 , String Num2){
        int a = Integer.parseInt(Num1);
        int b = Integer.parseInt(Num2);
        answer= a*b;
        return answer;
    }
    public int Division(String Num1 , String Num2){
        int a = Integer.parseInt(Num1);
        int b = Integer.parseInt(Num2);
        answer= a/b;
        return answer;
    }

}
