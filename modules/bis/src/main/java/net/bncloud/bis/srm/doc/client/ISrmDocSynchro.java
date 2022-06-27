package net.bncloud.bis.srm.doc.client;

import net.bncloud.bis.srm.doc.webservice.ArrayOfInt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ISrmDocSynchro {

    List<Map<String,Object>> getDocInfoList(Integer in0,ArrayOfInt in1);

    List<Map<String,Object>> getDocFileList(Integer in0, ArrayOfInt in1);
}
