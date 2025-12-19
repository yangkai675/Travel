package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dto.ai.QwenRequest;
import com.tencent.wxcloudrun.dto.ai.QwenResponse;
import com.tencent.wxcloudrun.dto.travel.TravelPlanResponse;
import com.tencent.wxcloudrun.dto.travel.TravelRequest;
import com.tencent.wxcloudrun.service.QwenService;
import com.tencent.wxcloudrun.service.TravelPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 旅游攻略生成服务实现类
 */
@Service
public class TravelPlanServiceImpl implements TravelPlanService {

    private static final Logger logger = LoggerFactory.getLogger(TravelPlanServiceImpl.class);

    @Autowired
    private QwenService qwenService;

    @Override
    public TravelPlanResponse generateTravelPlan(TravelRequest request) {
        TravelPlanResponse response = new TravelPlanResponse();

        try {
            logger.info("开始生成旅游攻略: {} -> {}, {}天, {}人, 预算¥{}",
                    request.getFromCity(), request.getToCity(),
                    request.getDays(), request.getPeople(), request.getBudget());

            // 1. 标准化城市名
            String fromCity = normalizeCity(request.getFromCity());
            String toCity = normalizeCity(request.getToCity());

            // 2. 构造Prompt（暂不查询真实API，由AI自行规划）
            String prompt = buildPrompt(request, fromCity, toCity);

            // 3. 调用千问AI
            QwenRequest qwenRequest = new QwenRequest();
            qwenRequest.setPrompt(prompt);
            qwenRequest.setSystemPrompt(buildSystemPrompt());
            qwenRequest.setTemperature(0.8); // 更有创意
            qwenRequest.setMaxTokens(3000);  // 旅游攻略需要较长内容

            QwenResponse qwenResponse = qwenService.generate(qwenRequest);

            // 4. 封装响应
            if (qwenResponse.getSuccess()) {
                response.setSuccess(true);
                response.setPlanContent(qwenResponse.getContent());
                response.setRequestId(qwenResponse.getRequestId());
                if (qwenResponse.getTokenUsage() != null) {
                    response.setTotalTokens(qwenResponse.getTokenUsage().getTotalTokens());
                }

                logger.info("旅游攻略生成成功, requestId: {}, tokens: {}",
                        qwenResponse.getRequestId(),
                        qwenResponse.getTokenUsage() != null ? qwenResponse.getTokenUsage().getTotalTokens() : 0);
            } else {
                response.setSuccess(false);
                response.setErrorMessage(qwenResponse.getErrorMessage());
                logger.error("旅游攻略生成失败: {}", qwenResponse.getErrorMessage());
            }

        } catch (Exception e) {
            logger.error("旅游攻略生成异常", e);
            response.setSuccess(false);
            response.setErrorMessage("系统错误: " + e.getMessage());
        }

        return response;
    }

    /**
     * 标准化城市名（去除"市"、"省"、"自治州"等后缀）
     */
    private String normalizeCity(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return cityName;
        }

        // 去除常见的行政区划后缀
        return cityName
                .replace("省", "")
                .replace("市", "")
                .replace("自治州", "")
                .replace("自治区", "")
                .replace("特别行政区", "")
                .replace("蒙古族", "")
                .replace("回族", "")
                .replace("维吾尔", "")
                .trim();
    }

    /**
     * 构造AI提示词
     */
    private String buildPrompt(TravelRequest request, String fromCity, String toCity) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请帮我规划一份详细的旅游攻略。\n\n");
        prompt.append("【旅游需求】\n");
        prompt.append("- 出发地：").append(request.getFromCity()).append("\n");
        prompt.append("- 目的地：").append(request.getToCity()).append("\n");
        prompt.append("- 游玩天数：").append(request.getDays()).append("天\n");

        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            prompt.append("- 出发日期：").append(request.getStartDate()).append("\n");
        }

        prompt.append("- 人数：").append(request.getPeople()).append("人\n");
        prompt.append("- 出行方式：").append(request.getTransport()).append("\n");
        prompt.append("- 总预算：¥").append(String.format("%.0f", request.getBudget())).append("\n");

        if (request.getSpecialNeeds() != null && !request.getSpecialNeeds().isEmpty()) {
            prompt.append("- 特殊需求：").append(request.getSpecialNeeds()).append("\n");
        }

        prompt.append("\n【规划要求】\n");
        prompt.append("1. 交通方案：根据出行方式推荐具体的交通方案（").append(request.getTransport()).append("）\n");
        prompt.append("2. 住宿推荐：推荐3家左右性价比高的酒店（评分4.5+，价格合理）\n");
        prompt.append("3. 景点安排：推荐5个左右必游景点，包含门票价格和开放时间\n");
        prompt.append("4. 详细行程：规划每日行程（上午/下午/晚上），包含具体时间和活动\n");
        prompt.append("5. 预算分配：细分交通、住宿、餐饮、门票等各项预算\n");
        prompt.append("6. 实用提醒：天气、穿衣、美食、注意事项等\n");
        prompt.append("7. 用亲切、朋友般的语气撰写，多用emoji增加趣味性\n\n");

        prompt.append("【重要提示】\n");
        prompt.append("- 所有信息仅供参考，实际出行请以官方信息为准\n");
        prompt.append("- 门票价格、交通费用等可能有变化\n");
        prompt.append("- 建议提前预订酒店和门票\n");

        return prompt.toString();
    }

    /**
     * 构造系统角色提示词
     */
    private String buildSystemPrompt() {
        return "你是一位经验丰富的旅游规划师，擅长为用户量身定制旅游攻略。" +
                "你会根据用户的预算、时间、人数等需求，提供详细实用的旅游建议。" +
                "你的攻略会包含交通、住宿、景点、美食等全方位信息，" +
                "并且用亲切、朋友般的语气书写，让用户感到温暖和信赖。" +
                "你会在合适的地方使用emoji，让攻略更生动有趣。" +
                "你提供的所有价格、时间等信息都会标注"+"仅供参考，请以实际为准"+"。";
    }
}