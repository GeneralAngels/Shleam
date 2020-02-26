package demo.bank;

import com.ga2230.shleam.Shleam;
import com.ga2230.shleam.base.structure.Function;
import com.ga2230.shleam.base.structure.Module;
import com.ga2230.shleam.base.structure.Result;

import java.util.HashMap;

public class Bank {

    public static void main(String[] arguments) {
        Shleam.begin(8000, new MyBank());
    }

    static class MyBank extends Module {

        HashMap<String, Integer> list = new HashMap<>();

        public MyBank() {
            super("bank");

            // Add people to my list
            list.put("shleam", 1337);

            // Expose my function
            register("get_cash", name -> Result.finished(list.get(name).toString()));
        }
    }

}
