package reqParser;

import util.ModeEnum;
import util.PathConsts;
import util.TypeEnum;

import java.io.File;

/**
 * @author Guo Weize
 * @date 2021/6/27
 */
public class RequirementParser {

    public static void run() {
        String filePath = PathConsts.file(ModeEnum.REQUIREMENT, TypeEnum.LANGUAGE);
        File file = new File(filePath);


    }



}
