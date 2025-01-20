import java.util.Map;

public class ReturnStatement extends Statement {

    private Expression exp;
    
    public ReturnStatement (Token returnToken, Expression e) {
        super(returnToken._position);
        exp = e;
    }

    @Override
    public void bind (Procedure procedure, Map<String, Declaration> symbols) {

	setProcedure(procedure);
	if (exp != null) {
	    exp.bind(procedure, symbols);
	}
	
    } // bind ()

    @Override
    public Type verify () {

	if (exp != null) {
	    
	    exp.verify();
	    if (!exp.getType().equals(getProcedure()._type)) {
		Utility.error("Return type " + exp.getType() + " does not match required type " + getProcedure()._type, _position);
	    }
	    
	}

	return getProcedure()._type;
	
    }

    @Override
    public String toAssembly() {
        String assembly = new String();
        if (exp != null) {
            assembly += exp.toAssembly();
            assembly += "\tpop rax ; Save return value in rax\n";
        }
        assembly += "\tjmp\treturn_" + getProcedure()._name + "\t; Return from procedure PROCEDURENAME\n";
        return assembly;
    }

    @Override
    public String toStatics() {
        return exp.toStatics();
    }

    public String toString() {
	return "return" + (exp == null ? "" : " " + exp);
    }
}
