*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure

*** Test Cases ***

PATCH-EVENT-TICKET-TYPE-001 - Validar atualização de nome do ticket type
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (nome) → Validar
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    ${novo_nome}=    Set Variable    Ingresso VIP Atualizado
    &{payload}=    Criar Payload Atualizacao Parcial    name    ${novo_nome}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}
    Should Be Equal    ${response.json()['name']}    ${novo_nome}

PATCH-EVENT-TICKET-TYPE-002 - Validar atualização de preço do ticket type
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (preço) → Validar
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    &{payload}=    Criar Payload Atualizacao Parcial    price    ${200}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}
    Should Be Equal As Numbers    ${response.json()['price']}    200

PATCH-EVENT-TICKET-TYPE-003 - Validar atualização de capacidade dentro do limite
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (capacidade) → Validar
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    &{payload}=    Criar Payload Atualizacao Parcial    capacity     2
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}

PATCH-EVENT-TICKET-TYPE-004 - Validar falha ao atualizar com capacidade inferior aos ingressos já comprados
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    end-to-end    mercado-pago-api
    [Documentation]    Fluxo COMPLETO: evento → ticket type → pagamento via API
    
    ${response_compra}    ${ticket_type_id}=    Executar Fluxo Completo Pagamento    3
    Validar_Response_Compra_Sucesso    ${response_compra}
    ${response_json}=    Set Variable    ${response_compra.json()}
    &{payload}=    Criar Payload Atualizacao Parcial    capacity    2
    Sleep    15s
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${409}

PATCH-EVENT-TICKET-TYPE-005 - Validar falha ao usar nome duplicado no mesmo evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Evento → 2 Tickets → PATCH (nome duplicado) → Validar falha
    
    ${event_id}    ${ticket_type_1}    ${ticket_type_2}    ${nome_ticket_1}=    Criar Dois Tickets No Mesmo Evento
    &{payload}=    Criar Payload Atualizacao Parcial    name    ${nome_ticket_1}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_2}    ${payload}    ${409}
    
PATCH-EVENT-TICKET-TYPE-006 - Validar falha ao atualizar ticket type inexistente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Tentar PATCH em UUID inexistente → Validar falha 404
    
    ${uuid_inexistente}=    Set Variable    a1b2c3d4-e5f6-7890-abcd-ef1234567890
    &{payload}=    Create Dictionary    name=Teste Atualizacao
    ${response}=    Executar Patch Ticket Type    ${uuid_inexistente}    ${payload}    ${404}

PATCH-EVENT-TICKET-TYPE-007 - Validar falha ao exceder capacidade total do evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (capacidade excessiva) → Validar falha
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    &{payload}=    Criar Payload Atualizacao Parcial    capacity    ${150}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${409}

PATCH-EVENT-TICKET-TYPE-008 - Validar atualização com datas válidas
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (datas válidas) → Validar
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    &{payload}=    Create Dictionary
    ...    dateInitial=2030-10-10T12:00:00
    ...    dateFinal=2030-10-10T20:00:00
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    Validar Response Ticket Atualizado    ${response}    ${ticket_type_id}    ${event_id}

PATCH-EVENT-TICKET-TYPE-009 - Validar falha com datas fora do período do evento
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_FAILURE}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (datas inválidas) → Validar falha
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    ${data_invalida}=    Set Variable    2030-10-10T22:00:00
    &{payload}=    Criar Payload Atualizacao Parcial    dateFinal    ${data_invalida}
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${409}

PATCH-EVENT-TICKET-TYPE-010 - Validar atualização múltipla de campos
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}
    [Documentation]    Fluxo: Evento → Ticket → PATCH (múltiplos campos) → Validar
    
    ${event_id}    ${ticket_type_id}=    Criar Evento E Ticket Para Atualizacao
    &{payload}=    Create Dictionary
    ...    name=Ingresso Premium
    ...    description=Descrição atualizada do ingresso
    ...    price=300
    ...    capacity=2
    ${response}=    Executar Patch Ticket Type    ${ticket_type_id}    ${payload}    ${200}
    
