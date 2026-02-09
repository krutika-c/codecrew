package com.codecrew.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import android.text.method.LinkMovementMethod

class ResourceProviderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_provider)

        val etTopic = findViewById<EditText>(R.id.etTopic)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val tvLinks = findViewById<TextView>(R.id.tvLinks)

        btnSearch.setOnClickListener {
            val topic = etTopic.text.toString().trim()

            if (topic.isEmpty()) {
                tvDescription.text = "Please enter a topic"
                tvLinks.text = ""
            } else {
                tvDescription.text = generateDescription(topic)
                tvLinks.text = HtmlCompat.fromHtml(generateLinks(topic), HtmlCompat.FROM_HTML_MODE_LEGACY)
                tvLinks.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private fun generateDescription(topic: String): String {


        return when (topic.lowercase()) {


            "data structures", "dsa" -> """
       Data Structures and Algorithms (DSA) is a fundamental subject in computer science that focuses on how data can be stored, organized, and manipulated efficiently to solve computational problems.
       It teaches various types of data structures including arrays, linked lists, stacks, queues, trees, graphs, heaps, and hash tables, along with their applications.
       Algorithmic techniques such as searching, sorting, recursion, dynamic programming, and greedy strategies are core components of DSA.
       Understanding DSA allows developers to optimize both time and space complexity of programs, which is essential for real-world applications where efficiency matters.
       Mastery of DSA strengthens logical thinking, problem-solving skills, and coding proficiency.
       This subject is crucial for competitive programming, technical interviews, and designing scalable software solutions.
       Learning DSA also helps in understanding complex systems and prepares students to tackle advanced topics like databases, networking, and artificial intelligence.
       In summary, DSA forms the backbone of a computer scientist's skill set and is indispensable for anyone aspiring to excel in software development or research.
   """.trimIndent()


            "operating system", "os" -> """
       Operating System (OS) is a core subject in computer science that studies the software responsible for managing computer hardware and software resources.
       The OS acts as an intermediary between the user and the machine, ensuring that multiple applications can run simultaneously without conflicts.
       Key topics include process management, CPU scheduling, memory management, file systems, input/output device management, and security mechanisms.
       Students learn about concepts like multitasking, multithreading, deadlocks, virtual memory, and synchronization to understand how modern systems operate efficiently.
       Knowledge of operating systems is essential for system programming, performance optimization, and understanding how software interacts with hardware.
       Popular operating systems such as Windows, Linux, macOS, and Android are used as case studies to explain real-world implementations.
       By studying OS, students develop analytical skills to design robust and efficient software solutions while understanding the underlying structure of computing systems.
       Overall, OS is a critical subject that underpins software development, networking, and computing infrastructure.
   """.trimIndent()


            "dbms", "database management system" -> """
       Database Management System (DBMS) is a comprehensive subject focused on the efficient storage, retrieval, and management of structured data.
       It enables multiple users to access databases securely while maintaining data integrity and consistency.
       Core concepts include relational databases, tables, normalization, indexing, transactions, concurrency control, and backup/recovery mechanisms.
       Structured Query Language (SQL) is used to interact with DBMS for data manipulation and retrieval.
       Students learn to design database schemas, implement queries, and ensure optimal performance of databases under real-world workloads.
       DBMS finds applications in diverse industries such as banking, healthcare, e-commerce, and education, making it a highly practical and in-demand skill.
       Understanding DBMS allows developers to build robust, scalable, and secure software systems while supporting analytics and reporting functionalities.
       Overall, DBMS is an essential component of computer science education, providing the foundation for data-driven application development and enterprise solutions.
   """.trimIndent()


            "computer networks", "networks" -> """
       Computer Networks is a vital subject that deals with the principles, design, and implementation of communication between computers and devices over various types of media.
       It introduces networking models such as OSI and TCP/IP, along with fundamental concepts like IP addressing, routing, switching, and subnetting.
       Students study networking protocols including HTTP, FTP, DNS, SMTP, and SNMP, as well as modern concepts like network security, encryption, firewalls, and VPNs.
       Understanding how networks operate allows students to design, implement, and manage communication systems efficiently, ensuring reliable data transfer and connectivity.
       Networking skills are essential for careers in system administration, cloud computing, cybersecurity, and distributed systems.
       Practical knowledge includes configuring routers and switches, troubleshooting connectivity issues, and monitoring network performance.
       Overall, computer networks provide the foundation for internet communication, cloud services, IoT devices, and all modern interconnected computing environments, making it a critical area of study in computer science.
   """.trimIndent()


            "java" -> """
       Java is a widely used, high-level, object-oriented programming language known for its platform independence through the Java Virtual Machine (JVM).
       It is extensively used in enterprise software, web applications, Android development, and backend systems.
       Core concepts include object-oriented programming principles such as classes, objects, inheritance, polymorphism, abstraction, and encapsulation.
       Java also supports multithreading, exception handling, collections framework, and input/output operations, providing a robust foundation for building large-scale software.
       Students learn to write efficient, maintainable, and reusable code while developing applications ranging from simple programs to complex enterprise systems.
       Knowledge of Java is highly valued in the IT industry due to its versatility and wide adoption.
       Mastering Java enables students to understand advanced programming paradigms, work on Android projects, and prepare for technical interviews.
       Overall, Java is a critical language that bridges theoretical knowledge with practical software development skills.
   """.trimIndent()


            "python" -> """
       Python is a high-level, interpreted, general-purpose programming language renowned for its simplicity and readability.
       It supports multiple programming paradigms, including procedural, object-oriented, and functional programming.
       Python has an extensive standard library and rich ecosystem of third-party packages for web development, data analysis, machine learning, automation, and scientific computing.
       Students learn to write clean, concise, and maintainable code while leveraging frameworks like Django, Flask, NumPy, and Pandas.
       Python's versatility and ease of learning make it an ideal first programming language for beginners, while also being powerful enough for advanced applications.
       Knowledge of Python is highly valuable for careers in data science, artificial intelligence, web development, and research.
       Mastery of Python enhances problem-solving skills and provides a strong foundation for developing modern software applications.
   """.trimIndent()


            "artificial intelligence", "ai" -> """
       Artificial Intelligence (AI) is the branch of computer science focused on creating intelligent systems capable of performing tasks that typically require human intelligence.
       AI encompasses machine learning, deep learning, natural language processing (NLP), computer vision, robotics, and expert systems.
       Students learn to design algorithms that enable systems to perceive, reason, learn, and make decisions.
       AI applications include speech recognition, recommendation systems, autonomous vehicles, and intelligent assistants.
       Understanding AI concepts allows students to solve complex problems, automate processes, and analyze large datasets efficiently.
       The field of AI is rapidly growing, driving innovation across industries such as healthcare, finance, transportation, and education.
       Mastery of AI provides opportunities for research, development, and contributing to cutting-edge technologies shaping the future of computing.
   """.trimIndent()


            "machine learning", "ml" -> """
       Machine Learning (ML) is a subset of artificial intelligence focused on building algorithms that allow computers to learn patterns and make predictions from data without explicit programming.
       ML includes supervised, unsupervised, and reinforcement learning techniques.
       Core concepts include regression, classification, clustering, decision trees, neural networks, and deep learning.
       Students learn to preprocess data, train models, evaluate performance, and deploy solutions for real-world applications.
       ML is widely used in recommendation systems, fraud detection, predictive analytics, image and speech recognition, and autonomous systems.
       Understanding ML equips students with the skills to develop intelligent applications, analyze large datasets, and contribute to innovations in AI-driven technologies.
       Knowledge of ML is highly valuable for careers in data science, research, and software development.
   """.trimIndent()


            "mathematics", "discrete mathematics" -> """
       Discrete Mathematics is a crucial subject for computer science students, providing the mathematical foundation for algorithms, programming, and computing theory.
       It covers topics such as logic, set theory, combinatorics, graph theory, number theory, and probability.
       These concepts are used to analyze algorithms, design data structures, and understand computational complexity.
       Discrete mathematics enables students to model problems, reason formally, and develop proofs, which are essential skills for problem-solving in computer science.
       Mastery of this subject enhances logical thinking, precision, and analytical abilities.
       It also forms the basis for cryptography, computer networks, database theory, and artificial intelligence.
       Understanding discrete mathematics is indispensable for both academic success and professional competence in computing fields.
   """.trimIndent()


            "projects", "major project", "labs" -> """
       Practical labs and projects are an essential part of computer science education, allowing students to apply theoretical knowledge in real-world scenarios.
       Programming labs include exercises in C, Java, Python, and other languages to build problem-solving skills.
       Core labs cover subjects like DSA, DBMS, OS, and networking, providing hands-on experience with data structures, database operations, process management, and network configurations.
       Hardware labs such as Digital Logic Design and Microprocessor Lab give students practical exposure to computing devices and circuits.
       Major projects and capstone experiences encourage students to integrate multiple technologies to solve real problems, develop software applications, and collaborate in teams.
       These practical experiences enhance technical proficiency, creativity, critical thinking, and prepare students for professional work and internships.
       Engaging in projects also strengthens understanding of software development life cycles and project management skills.
   """.trimIndent()


            else -> """
       $topic is an important subject in computer science and engineering.
       Studying $topic helps students build a strong foundation in technical knowledge, problem-solving, and analytical skills.
       It prepares learners for academic success, professional development, and real-world applications.
       Students are encouraged to explore detailed resources, textbooks, tutorials, and online platforms to gain a comprehensive understanding of $topic.
       Integrating $topic knowledge with hands-on projects and practice enhances mastery and ensures readiness for technical challenges, interviews, and industry requirements.
   """.trimIndent()
        }
    }



    private fun generateLinks(topic: String): String {
        val dashFormat = topic.trim().replace(" ", "-")
        val underscoreFormat = topic.trim().replace(" ", "_")
        val plusFormat = topic.trim().replace(" ", "+")

        return """
            <a href="https://www.google.com/search?q=$plusFormat">Google Search for $topic</a><br><br>
            <a href="https://www.geeksforgeeks.org/$dashFormat">GeeksForGeeks - $topic</a><br><br>
            <a href="https://www.tutorialspoint.com/$underscoreFormat">Tutorialspoint - $topic</a><br><br>
            <a href="https://en.wikipedia.org/wiki/$underscoreFormat">Wikipedia - $topic</a>
        """.trimIndent()
    }
}
