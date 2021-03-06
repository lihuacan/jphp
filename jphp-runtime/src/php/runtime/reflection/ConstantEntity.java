package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.reflection.support.Entity;

public class ConstantEntity extends Entity {

    protected ModuleEntity module;
    protected ClassEntity clazz;

    protected DocumentComment docComment;
    protected Memory value;
    public final boolean caseSensitise;

    public ConstantEntity(Context context) {
        super(context);
        this.caseSensitise = true;
        this.name = null;
    }

    public ConstantEntity(String name, Memory value, boolean caseSensitise) {
        super(null);
        this.setName(name);
        this.value = value;
        this.caseSensitise = caseSensitise;
    }

    public boolean isDeprecated(){
        return false; // TODO
    }

    public DocumentComment getDocComment() {
        return docComment;
    }

    public void setDocComment(DocumentComment docComment) {
        this.docComment = docComment;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        updateInternalName();
    }

    protected void updateInternalName(){
        if (name != null){
            if (clazz != null)
                internalName = "\0" + clazz.getLowerName() + "##" + (caseSensitise ? lowerName : name);
            else if (module != null)
                internalName = "\0" + module.getInternalName() + "##" + (caseSensitise ? lowerName : name);
        }
    }

    public void setClazz(ClassEntity clazz) {
        this.clazz = clazz;
        this.module = clazz == null ? null : clazz.getModule();
        updateInternalName();
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
        updateInternalName();
    }

    public Memory getValue() {
        return value;
    }

    public Memory getValue(Environment env){
        if (value == null)
            return env.getStatic(internalName).toValue();
        else
            return value;
    }

    public void setValue(Memory value) {
        synchronized (this){
            this.value = value;
        }
    }

    public boolean isCaseSensitise() {
        return caseSensitise;
    }

    public boolean isOwned(ClassEntity entity){
        return clazz != null && clazz.getId() == entity.getId();
    }
}
