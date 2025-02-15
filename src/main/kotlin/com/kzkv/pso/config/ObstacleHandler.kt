package com.kzkv.pso.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.kzkv.pso.data.AggregateResult
import com.kzkv.pso.data.Vector
import com.kzkv.pso.entity.Obstacle
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArrayList

@Component
class ObstacleHandler(private val objectMapper: ObjectMapper) : TextWebSocketHandler() {
    private val sessions = CopyOnWriteArrayList<WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: org.springframework.web.socket.CloseStatus) {
        sessions.remove(session)
    }

    fun broadcastObjects(obstacles: List<Obstacle>, route: List<Vector>): AggregateResult {
        val result = AggregateResult(obstacles, route)
        val message = objectMapper.writeValueAsString(AggregateResult(obstacles, route))
        sessions.forEach { it.sendMessage(TextMessage(message)) }
        return result
    }
}
