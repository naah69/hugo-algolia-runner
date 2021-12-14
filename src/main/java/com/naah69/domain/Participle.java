package com.naah69.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.naah69.infrastructure.config.command.sub.CompileBlogConfig;
import com.naah69.infrastructure.model.Algolia;
import com.naah69.infrastructure.utils.FileUtils;
import com.naah69.infrastructure.utils.YamlUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@AllArgsConstructor
public class Participle {

    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final CompileBlogConfig.ParticipleConfig participleConfig;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void participle() {
        //分词
        List<File> mdList = getMarkDownList();
        List<String> disableList = getStopWordList();
        List<Algolia> algoliaList = getAlgoliasList();

        Map<String, String> participlesMap = new ConcurrentHashMap<>(algoliaList.size());

        CountDownLatch latch = new CountDownLatch(algoliaList.size());

        for (File file : mdList) {

            fixedThreadPool.execute(() -> {
                String article = FileUtils.readFileByLines(file);
                try {
                    //todo 优化分词代码
                    //todo 加入md5校验优化分词速度
                    String articleParam = article.substring(0, article.indexOf("---", 4));
                    article = article.substring(article.indexOf("---", 4) + 3, article.length());
                    Map<String, Object> articleYaml = YamlUtils.convertToMap(articleParam);
                    String title = articleYaml.get("title").toString();
                    String participles = getParticiples(disableList, article);
                    participlesMap.put(title, participles);
                    log.info("[PARTICIPLE] participle success: {}", file.getAbsolutePath());
                } catch (Exception e) {
                    log.info("[PARTICIPLE] participle error: {}", file.getAbsolutePath());
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        for (Algolia algolia : algoliaList) {
            String participles = participlesMap.get(algolia.getTitle());
            algolia.setContent(algolia.getContent() + " " + participles);
        }
    }


    /**
     * 获取markdown列表
     *
     * @return markdown列表
     */
    private List<File> getMarkDownList() {
        List<File> mdList = FileUtils.getAllFiles(participleConfig.markdownDir());
        for (Iterator<File> fileIterator = mdList.iterator(); fileIterator.hasNext(); ) {
            File next = fileIterator.next();
            if (!next.getAbsolutePath().endsWith(".md")) {
                fileIterator.remove();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("[PARTICIPLE] markdown count: {}", mdList.size());
        }
        return mdList;
    }

    /**
     * 获取停用表
     *
     * @return 停用表
     */
    private List<String> getStopWordList() {
        Optional<String> s = participleConfig.stopPath();
        List<String> result = List.of();
        if (s.isPresent()) {
            String disable = FileUtils.readFileByLines(new File(s.get()));
            String[] split = disable.split(",");
            result = Arrays.asList(split);
        }
        if (log.isDebugEnabled()) {
            log.debug("[PARTICIPLE] stop word count: {}", result.size());
        }
        return result;
    }


    /**
     * 获取Algolias列表
     *
     * @return Algolias列表
     */
    @SneakyThrows
    private List<Algolia> getAlgoliasList() {
        //todo 更新Algolia结构
        String algoliaJson = FileUtils.readFileByLines(new File(participleConfig.algoliaJsonPath()));
        return objectMapper.readValue(algoliaJson, new TypeReference<List<Algolia>>() {
        });
    }


    /**
     * 获取分词
     *
     * @param stopWordList 停用词列表
     * @param str         文本
     * @return 分词文本（空格间隔）
     */
    private String getParticiples(List<String> stopWordList, String str) {

        List<Term> termList = new ViterbiSegment().enableIndexMode(true).seg(str);
        HashSet<String> wordSet = new HashSet<>();
        for (Term term : termList) {
            wordSet.add(term.word);
        }
        wordSet.removeAll(stopWordList);
        StringBuilder sb = new StringBuilder();
        for (String word : wordSet) {
            sb.append(word).append(" ");
        }
        return sb.toString().replaceAll("\n", "");
    }

}
