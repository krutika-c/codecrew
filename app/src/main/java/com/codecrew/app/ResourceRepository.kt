package com.codecrew.app

object ResourceRepository {

    fun getResource(topic: String): TopicResource {

        return when(topic.lowercase()) {

            "time management" -> TopicResource(
                "Time Management",
                "Time management is the process of planning and organizing how to divide your time between specific activities. Effective time management enables people to work smarter rather than harder so that they get more done in less time. It helps reduce stress and increases productivity. Proper time management allows individuals to prioritize important tasks and avoid unnecessary distractions. By using different strategies such as scheduling, planning, and setting goals, a person can achieve better results in academics and professional life. Learning to manage time properly is a valuable life skill that helps in building discipline and responsibility. With good time management habits, people can maintain a healthy balance between work and personal life. It also improves decision-making and helps individuals focus on their long-term objectives and career growth.",
                listOf(
                    "https://www.mindtools.com/pages/article/newHTE_00.htm",
                    "https://www.skillsyouneed.com/ps/time-management.html"
                )
            )

            "eisenhower matrix" -> TopicResource(
                "Eisenhower Matrix",
                "The Eisenhower Matrix is a task management and decision-making tool that helps people organize their work based on urgency and importance. It divides tasks into four different categories: urgent and important, important but not urgent, urgent but not important, and neither urgent nor important. This method helps users clearly understand which tasks require immediate attention and which tasks can be scheduled for later. By following this technique, individuals can avoid wasting time on unnecessary activities and focus more on productive work. It is widely used by students and professionals to improve productivity and reduce workload stress. The Eisenhower Matrix encourages better planning and smart prioritization. Using this system regularly can help people become more organized, disciplined, and goal-oriented in their daily routine.",
                listOf(
                    "https://www.eisenhower.me/eisenhower-matrix/",
                    "https://todoist.com/productivity-methods/eisenhower-matrix"
                )
            )

            else -> TopicResource(
                topic,
                generateGenericDescription(topic),
                listOf("https://www.google.com/search?q=$topic")
            )
        }
    }

    fun generateGenericDescription(topic: String): String {
        return "$topic is an important concept that plays a major role in learning and personal development. Understanding $topic can help individuals improve their skills and knowledge in a structured way. This topic includes various methods, techniques, and strategies that support better productivity and growth. Learning about $topic allows users to become more confident and efficient in their daily activities. It helps in improving problem-solving ability and encourages continuous self-improvement. Proper knowledge of $topic can be useful for students, professionals, and anyone who wants to develop better habits. By exploring different resources and practicing regularly, a person can gain deeper understanding of $topic and apply it effectively in real-life situations."
    }
}
