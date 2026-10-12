package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SkillspotRepository(private val dao: SkillspotDao) {

    // Seeds initial data if the database is empty
    suspend fun seedInitialDataIfEmpty() {
        val existingJobs = dao.getAllJobs().first()
        if (existingJobs.isEmpty()) {
            dao.insertJobs(initialJobs)
            dao.insertCandidates(initialCandidates)
            dao.insertPortfolioProjects(initialPortfolioProjects)
            dao.insertWorkshops(initialWorkshops)
            dao.insertMessages(initialMessages)
            dao.insertApplication(
                ApplicationEntity(
                    id = "app_init_1",
                    jobId = "job_1",
                    jobTitle = "Android & Compose Engineering Intern",
                    company = "NovaTech Labs",
                    candidateName = "Alex Rivera",
                    candidateHeadline = "Mobile Developer & UI Systems Enthusiast",
                    pitch = "Built 3 production Jetpack Compose apps with M3 and Coroutines. Excited to build responsive UI pipelines.",
                    attachedProjectTitle = "Skillspot Mobile Client",
                    status = ApplicationStatus.INTERVIEW_SCHEDULED.name,
                    appliedDate = "Yesterday"
                )
            )
        }
    }

    // JOBS
    fun getJobs(): Flow<List<JobPosting>> = dao.getAllJobs().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun toggleSaveJob(jobId: String, currentSaved: Boolean) {
        dao.updateJobSavedStatus(jobId, !currentSaved)
    }

    suspend fun postJob(
        title: String,
        company: String,
        location: String,
        isRemote: Boolean,
        type: ExperienceLevel,
        stipendOrSalary: String,
        duration: String,
        requiredSkills: List<String>,
        description: String,
        experienceGained: String,
        mentorName: String,
        mentorRole: String
    ) {
        val newJob = JobEntity(
            id = "job_" + UUID.randomUUID().toString().take(8),
            title = title,
            company = company,
            location = location,
            isRemote = isRemote,
            type = type.name,
            stipendOrSalary = stipendOrSalary,
            duration = duration,
            requiredSkillsCsv = requiredSkills.joinToString(","),
            description = description,
            experienceGained = experienceGained,
            mentorName = mentorName,
            mentorRole = mentorRole,
            applicantsCount = 0,
            isSaved = false,
            isPostedByUser = true
        )
        dao.insertJob(newJob)
    }

    // CANDIDATES
    fun getCandidates(): Flow<List<Candidate>> = dao.getAllCandidates().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun toggleBookmarkCandidate(candidateId: String, currentBookmarked: Boolean) {
        dao.updateCandidateBookmark(candidateId, !currentBookmarked)
    }

    // PORTFOLIO
    fun getPortfolioProjects(): Flow<List<PortfolioProject>> = dao.getAllPortfolioProjects().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun addPortfolioProject(
        title: String,
        role: String,
        description: String,
        techStack: List<String>,
        impactMetric: String,
        liveUrl: String,
        githubUrl: String
    ) {
        val newProj = PortfolioEntity(
            id = "proj_" + UUID.randomUUID().toString().take(8),
            title = title,
            role = role,
            description = description,
            techStackCsv = techStack.joinToString(","),
            impactMetric = impactMetric,
            liveUrl = liveUrl,
            githubUrl = githubUrl,
            createdAt = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
        )
        dao.insertPortfolioProject(newProj)
    }

    suspend fun deletePortfolioProject(id: String) {
        dao.deletePortfolioProject(id)
    }

    // APPLICATIONS
    fun getApplications(): Flow<List<JobApplication>> = dao.getAllApplications().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun submitApplication(
        job: JobPosting,
        candidateName: String,
        candidateHeadline: String,
        pitch: String,
        attachedProjectTitle: String
    ) {
        val app = ApplicationEntity(
            id = "app_" + UUID.randomUUID().toString().take(8),
            jobId = job.id,
            jobTitle = job.title,
            company = job.company,
            candidateName = candidateName,
            candidateHeadline = candidateHeadline,
            pitch = pitch,
            attachedProjectTitle = attachedProjectTitle,
            status = ApplicationStatus.UNDER_REVIEW.name,
            appliedDate = "Just now"
        )
        dao.insertApplication(app)
        dao.incrementJobApplicantCount(job.id)
    }

    suspend fun updateApplicationStatus(applicationId: String, newStatus: ApplicationStatus) {
        dao.updateApplicationStatus(applicationId, newStatus.name)
    }

    // WORKSHOPS
    fun getWorkshops(): Flow<List<Workshop>> = dao.getAllWorkshops().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun enrollInWorkshop(workshopId: String) {
        val credential = "SKILL-VERIFIED-" + UUID.randomUUID().toString().take(6).uppercase()
        dao.updateWorkshopEnrollment(
            workshopId = workshopId,
            isEnrolled = true,
            isCompleted = true, // Instant certification upon workshop enrollment/completion
            credentialId = credential
        )
    }

    // MESSAGES
    fun getMessages(): Flow<List<DirectMessage>> = dao.getAllMessages().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun sendMessage(
        conversationId: String,
        senderName: String,
        senderRole: UserRole,
        recipientName: String,
        text: String,
        jobTitleContext: String = ""
    ) {
        val msg = MessageEntity(
            id = "msg_" + UUID.randomUUID().toString().take(8),
            conversationId = conversationId,
            senderName = senderName,
            senderRole = senderRole.name,
            recipientName = recipientName,
            messageText = text,
            timestamp = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()),
            jobTitleContext = jobTitleContext
        )
        dao.insertMessage(msg)
    }

    // Mappers
    private fun JobEntity.toDomain() = JobPosting(
        id = id,
        title = title,
        company = company,
        location = location,
        isRemote = isRemote,
        type = runCatching { ExperienceLevel.valueOf(type) }.getOrDefault(ExperienceLevel.INTERNSHIP),
        stipendOrSalary = stipendOrSalary,
        duration = duration,
        requiredSkills = requiredSkillsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        description = description,
        experienceGained = experienceGained,
        mentorName = mentorName,
        mentorRole = mentorRole,
        applicantsCount = applicantsCount,
        isSaved = isSaved,
        isPostedByUser = isPostedByUser
    )

    private fun CandidateEntity.toDomain() = Candidate(
        id = id,
        name = name,
        headline = headline,
        avatarInitials = avatarInitials,
        location = location,
        bio = bio,
        skills = skillsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        yearsOfExperience = yearsOfExperience,
        availability = availability,
        completedProjectsCount = completedProjectsCount,
        rating = rating,
        isBookmarked = isBookmarked,
        githubUrl = githubUrl,
        portfolioUrl = portfolioUrl
    )

    private fun PortfolioEntity.toDomain() = PortfolioProject(
        id = id,
        title = title,
        role = role,
        description = description,
        techStack = techStackCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        impactMetric = impactMetric,
        liveUrl = liveUrl,
        githubUrl = githubUrl,
        createdAt = createdAt
    )

    private fun ApplicationEntity.toDomain() = JobApplication(
        id = id,
        jobId = jobId,
        jobTitle = jobTitle,
        company = company,
        candidateName = candidateName,
        candidateHeadline = candidateHeadline,
        pitch = pitch,
        attachedProjectTitle = attachedProjectTitle,
        status = runCatching { ApplicationStatus.valueOf(status) }.getOrDefault(ApplicationStatus.UNDER_REVIEW),
        appliedDate = appliedDate
    )

    private fun WorkshopEntity.toDomain() = Workshop(
        id = id,
        title = title,
        instructor = instructor,
        instructorTitle = instructorTitle,
        category = category,
        duration = duration,
        skillsTaught = skillsTaughtCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        description = description,
        certificateName = certificateName,
        credentialId = credentialId,
        isEnrolled = isEnrolled,
        isCompleted = isCompleted,
        startDate = startDate
    )

    private fun MessageEntity.toDomain() = DirectMessage(
        id = id,
        conversationId = conversationId,
        senderName = senderName,
        senderRole = runCatching { UserRole.valueOf(senderRole) }.getOrDefault(UserRole.EMPLOYEE),
        recipientName = recipientName,
        messageText = messageText,
        timestamp = timestamp,
        jobTitleContext = jobTitleContext
    )

    companion object {
        val initialJobs = listOf(
            JobEntity(
                id = "job_1",
                title = "Android & Compose Engineering Intern",
                company = "NovaTech Labs",
                location = "San Francisco, CA",
                isRemote = true,
                type = ExperienceLevel.INTERNSHIP.name,
                stipendOrSalary = "$48 / hr + Housing Stipend",
                duration = "12 Weeks (Summer)",
                requiredSkillsCsv = "Kotlin,Jetpack Compose,Coroutines,M3",
                description = "Join our mobile architecture team to build modern reactive user interfaces using Jetpack Compose and Kotlin Coroutines. You will collaborate directly with staff engineers and contribute code to 10M+ daily active users.",
                experienceGained = "Hands-on experience shipping production Compose code, UI benchmark optimization, and CI/CD pipelines.",
                mentorName = "Sarah Chen",
                mentorRole = "Staff Android Engineer",
                applicantsCount = 14,
                isSaved = true,
                isPostedByUser = false
            ),
            JobEntity(
                id = "job_2",
                title = "Junior AI Systems & LLM Integrations",
                company = "HyperScale AI",
                location = "New York, NY",
                isRemote = true,
                type = ExperienceLevel.JUNIOR.name,
                stipendOrSalary = "$95,000 / yr + Equity",
                duration = "Full-time Junior Role",
                requiredSkillsCsv = "Python,PyTorch,Gemini API,FastAPI,Docker",
                description = "Work on orchestrating multi-modal generative agents and real-time inference pipelines. Ideal for graduates and early-career engineers with proven project portfolios.",
                experienceGained = "Production RAG architectures, prompt evaluation frameworks, and low-latency API design.",
                mentorName = "David Vance",
                mentorRole = "VP of AI Research",
                applicantsCount = 28,
                isSaved = false,
                isPostedByUser = false
            ),
            JobEntity(
                id = "job_3",
                title = "Product Design & Design Systems Apprentice",
                company = "Vanguard Studios",
                location = "Austin, TX",
                isRemote = false,
                type = ExperienceLevel.INTERNSHIP.name,
                stipendOrSalary = "$38 / hr",
                duration = "16 Weeks",
                requiredSkillsCsv = "Figma,Design Systems,User Research,Prototyping",
                description = "Partner directly with product managers and engineers to craft cohesive, accessible, and high-impact digital experiences.",
                experienceGained = "Auditing design tokens, conducting moderated usability testing, and cross-functional handoffs.",
                mentorName = "Elena Rostova",
                mentorRole = "Lead Product Designer",
                applicantsCount = 9,
                isSaved = false,
                isPostedByUser = false
            ),
            JobEntity(
                id = "job_4",
                title = "Specialist Cloud Infra & Kubernetes Intern",
                company = "Aether Systems",
                location = "Seattle, WA",
                isRemote = true,
                type = ExperienceLevel.SPECIALIST.name,
                stipendOrSalary = "$52 / hr",
                duration = "14 Weeks",
                requiredSkillsCsv = "Kubernetes,Go,Terraform,GCP,Linux",
                description = "Build self-healing distributed clusters and automate zero-downtime microservice deployments across global regions.",
                experienceGained = "Real multi-region cloud resilience, Prometheus telemetry, and infrastructure as code.",
                mentorName = "Marcus Brody",
                mentorRole = "Principal SRE",
                applicantsCount = 19,
                isSaved = true,
                isPostedByUser = false
            ),
            JobEntity(
                id = "job_5",
                title = "Full-Stack Web & TypeScript Junior Engineer",
                company = "PulsePay Financial",
                location = "Chicago, IL",
                isRemote = true,
                type = ExperienceLevel.JUNIOR.name,
                stipendOrSalary = "$105,000 / yr",
                duration = "Full-time",
                requiredSkillsCsv = "TypeScript,React,Node.js,PostgreSQL,GraphQL",
                description = "Develop high-throughput payment checkout widgets and merchant dashboards. Work directly under senior architects.",
                experienceGained = "Financial ledger transactions, PCI compliance practices, and end-to-end testing.",
                mentorName = "Rachel Kim",
                mentorRole = "Head of Engineering",
                applicantsCount = 31,
                isSaved = false,
                isPostedByUser = false
            )
        )

        val initialCandidates = listOf(
            CandidateEntity(
                id = "cand_1",
                name = "Maya Lin",
                headline = "Mobile UI/UX Architect & Kotlin Developer",
                avatarInitials = "ML",
                location = "San Francisco, CA",
                bio = "Passionate about building fluid, accessible Android applications with Jetpack Compose. Author of 4 open-source UI libraries with 1.2k GitHub stars.",
                skillsCsv = "Kotlin,Jetpack Compose,Design Systems,Coroutines,MVI",
                yearsOfExperience = "1.5 yrs",
                availability = "Immediate (Internship/Junior)",
                completedProjectsCount = 6,
                rating = 4.9f,
                isBookmarked = true,
                githubUrl = "https://github.com/mayalin-dev",
                portfolioUrl = "https://mayalin.me"
            ),
            CandidateEntity(
                id = "cand_2",
                name = "Jordan Patel",
                headline = "Full-Stack & Cloud Systems Specialist",
                avatarInitials = "JP",
                location = "Toronto, ON",
                bio = "Distributed systems enthusiast with deep experience in Go, Rust, and Kubernetes. Built a custom distributed cache handling 100k req/s.",
                skillsCsv = "Go,Rust,Kubernetes,Docker,PostgreSQL,gRPC",
                yearsOfExperience = "2 yrs",
                availability = "Available in 2 Weeks",
                completedProjectsCount = 5,
                rating = 4.8f,
                isBookmarked = false,
                githubUrl = "https://github.com/jordanpatel-sys",
                portfolioUrl = "https://jordan.systems"
            ),
            CandidateEntity(
                id = "cand_3",
                name = "Sophia Nguyen",
                headline = "Applied AI & Computer Vision Researcher",
                avatarInitials = "SN",
                location = "Boston, MA",
                bio = "Master's graduate specializing in edge AI inference, PyTorch optimization, and multi-modal embeddings for mobile clients.",
                skillsCsv = "Python,PyTorch,TensorFlow Lite,FastAPI,Gemini API,ONNX",
                yearsOfExperience = "1 yr",
                availability = "Open to Summer Internship",
                completedProjectsCount = 4,
                rating = 5.0f,
                isBookmarked = true,
                githubUrl = "https://github.com/sophianguyen-ai",
                portfolioUrl = "https://sophia-ai.dev"
            ),
            CandidateEntity(
                id = "cand_4",
                name = "Liam O'Connor",
                headline = "Design Systems & Product Interaction Designer",
                avatarInitials = "LO",
                location = "London, UK",
                bio = "Crafting pixel-perfect design systems with tokenized design architecture, micro-interactions, and high-fidelity Figma components.",
                skillsCsv = "Figma,Prototyping,Design Systems,User Testing,CSS Tokens",
                yearsOfExperience = "2 yrs",
                availability = "Available for Contract / Full-Time",
                completedProjectsCount = 8,
                rating = 4.9f,
                isBookmarked = false,
                githubUrl = "https://github.com/liamdesigns",
                portfolioUrl = "https://liam.design"
            )
        )

        val initialPortfolioProjects = listOf(
            PortfolioEntity(
                id = "proj_1",
                title = "FinTrack Mobile Dashboard",
                role = "Lead Android Developer",
                description = "Modern personal finance tracker featuring custom Canvas charts, Room local offline caching, and biometric authentication.",
                techStackCsv = "Kotlin,Jetpack Compose,Room,Coroutines,Canvas",
                impactMetric = "+42% faster query rendering, 15k active installs",
                liveUrl = "https://fintrack.example.com",
                githubUrl = "https://github.com/alex/fintrack-android",
                createdAt = "Oct 2026"
            ),
            PortfolioEntity(
                id = "proj_2",
                title = "NeuroVision Real-Time Classifier",
                role = "AI Engineer",
                description = "Edge object detection pipeline running on Android using CameraX and TFLite with under 28ms latency per frame.",
                techStackCsv = "Python,TensorFlow Lite,CameraX,Kotlin",
                impactMetric = "98.4% validation accuracy with sub-30ms inference",
                liveUrl = "https://neurovision.example.com",
                githubUrl = "https://github.com/alex/neuro-vision",
                createdAt = "Sep 2026"
            ),
            PortfolioEntity(
                id = "proj_3",
                title = "Pulse Realtime Chat Engine",
                role = "Full-Stack Contributor",
                description = "WebSocket based messaging client with end-to-end typing indicators, offline retry queues, and optimistic UI updates.",
                techStackCsv = "WebSockets,Node.js,Compose,M3",
                impactMetric = "Handles 5,000 concurrent sockets without frame drops",
                liveUrl = "https://pulsechat.example.com",
                githubUrl = "https://github.com/alex/pulse-chat",
                createdAt = "Aug 2026"
            )
        )

        val initialWorkshops = listOf(
            WorkshopEntity(
                id = "ws_1",
                title = "Jetpack Compose Advanced Architecture & Animations",
                instructor = "Google Developer Experts (GDE)",
                instructorTitle = "Senior Android Architects",
                category = "Mobile Engineering",
                duration = "6 Weeks (Live + Lab)",
                skillsTaughtCsv = "Custom Layouts,Canvas,Shared Element Transitions,Compose Performance",
                description = "Master production-grade Jetpack Compose: state hoisting best practices, custom layout measures, smooth choreographies, and memory leak prevention.",
                certificateName = "Verified Certified Android Compose Specialist",
                credentialId = "SKILL-VERIFIED-COMPOSE-902",
                isEnrolled = true,
                isCompleted = true,
                startDate = "Enrollment Open"
            ),
            WorkshopEntity(
                id = "ws_2",
                title = "Full-Stack System Design & Microservices at Scale",
                instructor = "Danielle Reed",
                instructorTitle = "Ex-Meta Staff Infrastructure Engineer",
                category = "Backend & Cloud",
                duration = "4 Weeks",
                skillsTaughtCsv = "Distributed Caching,Kafka,Database Sharding,CAP Theorem",
                description = "Deep dive into real-world interview and industry problems: designing distributed rate limiters, global notifications, and fault-tolerant event streams.",
                certificateName = "System Design & Cloud Architecture Certificate",
                credentialId = "",
                isEnrolled = false,
                isCompleted = false,
                startDate = "Starts in 3 Days"
            ),
            WorkshopEntity(
                id = "ws_3",
                title = "GenAI & LLM Ops: Building Production Agents",
                instructor = "Kavita Sharma",
                instructorTitle = "Lead AI Engineer at Anthropic Labs",
                category = "Artificial Intelligence",
                duration = "5 Weeks",
                skillsTaughtCsv = "Gemini API,Prompt Chaining,Vector Embeddings,Evaluation Metrics",
                description = "Learn to build resilient multi-agent tools, prevent hallucinations, and deploy production endpoints with streaming responses.",
                certificateName = "Applied Generative AI Professional Certificate",
                credentialId = "",
                isEnrolled = false,
                isCompleted = false,
                startDate = "Starts next Monday"
            ),
            WorkshopEntity(
                id = "ws_4",
                title = "Enterprise Design Systems & Token Systems in Figma",
                instructor = "Marcus Weber",
                instructorTitle = "Design Systems Lead at Stripe",
                category = "Product & UI/UX",
                duration = "3 Weeks",
                skillsTaughtCsv = "Figma Variables,Design Tokens,Component Governance,Accessibility WCAG",
                description = "Build design systems that scale across thousands of designers and developers with bulletproof design tokens and documentation.",
                certificateName = "Design Systems & Token Architecture Credential",
                credentialId = "",
                isEnrolled = false,
                isCompleted = false,
                startDate = "Open for Cohort 8"
            )
        )

        val initialMessages: List<MessageEntity> = listOf(
            MessageEntity(
                id = "msg_1",
                conversationId = "conv_novatech",
                senderName = "Sarah Chen (NovaTech Labs)",
                senderRole = UserRole.EMPLOYER.name,
                recipientName = "Alex Rivera",
                messageText = "Hi Alex! We reviewed your FinTrack project and were really impressed by your Compose Canvas chart implementation. Would you be open for a 30-min technical chat for our Android Internship role?",
                timestamp = "10:15 AM",
                jobTitleContext = "Android & Compose Engineering Intern"
            ),
            MessageEntity(
                id = "msg_2",
                conversationId = "conv_novatech",
                senderName = "Alex Rivera",
                senderRole = UserRole.EMPLOYEE.name,
                recipientName = "Sarah Chen (NovaTech Labs)",
                messageText = "Hi Sarah, thank you for reaching out! I'd love to connect. I'm available this Thursday afternoon or Friday morning.",
                timestamp = "10:24 AM",
                jobTitleContext = "Android & Compose Engineering Intern"
            ),
            MessageEntity(
                id = "msg_3",
                conversationId = "conv_hyperscale",
                senderName = "David Vance (HyperScale AI)",
                senderRole = UserRole.EMPLOYER.name,
                recipientName = "Alex Rivera",
                messageText = "Hello! We noticed your verified workshop credential in AI Prompt Engineering. We are seeking talent with practical Python and Gemini API experience.",
                timestamp = "Yesterday",
                jobTitleContext = "Junior AI Systems & LLM Integrations"
            )
        )
    }
}
