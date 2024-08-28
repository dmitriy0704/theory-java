package dev.folomkin.core.oop.code;

class Code {
    public static void main(String[] args) {
        Challenger challenger = new JavaChallenger();
        challenger.doChallenge();
    }
}
interface Challenger {
    void doChallenge();
}

class JavaChallenger implements Challenger {
    @Override
    public void doChallenge() {
        System.out.println("Challenge done!");
    }
}
