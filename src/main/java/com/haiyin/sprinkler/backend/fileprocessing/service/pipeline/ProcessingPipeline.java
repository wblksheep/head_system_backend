//package com.haiyin.sprinkler.backend.fileprocessing.service.pipeline;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ProcessingPipeline {
//    private Function<MultipartFile, List<?>> pipeline;
//
//    public static ProcessingPipeline startWith(Parser parser) {
//        ProcessingPipeline pipeline = new ProcessingPipeline();
//        pipeline.pipeline = file -> parser.parse(file);
//        return pipeline;
//    }
//
//    public ProcessingPipeline thenConvert(Converter converter) {
//        pipeline = pipeline.andThen(list ->
//                list.stream().map(converter::convert).collect(Collectors.toList()));
//        return this;
//    }
//
//    public ProcessingPipeline thenSave(Saver saver) {
//        pipeline = pipeline.andThen(list -> {
//            saver.save(list);
//            return list;
//        });
//        return this;
//    }
//
//    // 场景专属流程配置
//    public static ProcessingPipeline createMaintainFlow() {
//        return startWith(new AllocateParser())
//                .thenConvert(new AllocateConverter())
//                .thenSave(new AllocateSaver())
//
//                .thenWith(new MaintainParser())
//                .thenConvert(new MaintainConverter())
//                .thenSave(new MaintainSaver());
//    }
//}
