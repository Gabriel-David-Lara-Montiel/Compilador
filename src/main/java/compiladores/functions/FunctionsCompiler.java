/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package compiladores.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Queue;
import java.util.LinkedList;

public class FunctionsCompiler {
    public String pseudoToCpp(String pseudoCode) {
        FunctionsBody fb = new FunctionsBody();
        FunctionsDeclaration fd = new FunctionsDeclaration();
        List<String> cppCode = new ArrayList<>();
        Queue<String> declarationsQueue = new LinkedList<>();
        Queue<String> bodiesQueue = new LinkedList<>();
        String mainFunction = "";
        String beginExpresion = "\\s*(FUNCION\\s*(\\w|\\W)*|INICIO)\\s*";
        String endExpresion = "\\s*FIN (FUNCION|INICIO)\\s*";
        boolean flag = false;
        boolean mainFlag = false; //added
        int lineCounter = 0;

        String[] fullPseudo = pseudoCode.toString().trim().split("\\n", -1);
        for (int i = 0; i < fullPseudo.length; i++) {
            lineCounter++;
            declarationsQueue.offer(fd.declare(fullPseudo[i], fb.getFunctionsNames()));
            bodiesQueue.offer(fb.body(fullPseudo[i], flag));
            if (Pattern.compile(beginExpresion).matcher(fullPseudo[i]).find() && !flag) {
                flag = true;
            } else if (Pattern.compile(endExpresion).matcher(fullPseudo[i]).find() && flag) {
                flag = false;
            }
            mainFlag = fb.isCloseMain() != fb.isMain();
        }
        while (!declarationsQueue.isEmpty()) {
            cppCode.add(declarationsQueue.poll());
        }
        cppCode.add("\n");
        while (!bodiesQueue.isEmpty()) {
            cppCode.add(bodiesQueue.poll());
        }
        cppCode.add("\n");
        
        String cppString = String.join("", cppCode.toArray(new String[0])).trim();

        return cppString;
    }
}
